package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationIsNotQualifiedException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.operation.AssociationsDoNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static pl.edu.pjatk.mas.mp02.model.association.AssociationsMetadataCacheHolder.resolveByTargetAndIdentifier;

public abstract class AssociatedObject {
    public static final String DEFAULT_ASSOCIATION_ID = "";

    private final Map<AssociationMetadata, Map<Object, AssociatedObject>> associations = new ConcurrentHashMap<>();

    public void link(AssociatedObject target, String id) throws AssociationAlreadyExistsException, AssociationMultiplicityException {
        link(target, id, 2);
    }

    public void link(AssociatedObject target) throws AssociationAlreadyExistsException, AssociationMultiplicityException {
        link(target, DEFAULT_ASSOCIATION_ID,2);
    }

    private void link(AssociatedObject target, String id, int callCounter) throws AssociationAlreadyExistsException, AssociationMultiplicityException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), target.getClass(), id);
        Map<Object, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new ConcurrentHashMap<>());

        var key = findQualifier(metadata, target).orElse(target);
        if (links.containsKey(key))
            throw new AssociationAlreadyExistsException(this, target, key);
        if (links.size() == metadata.max())
            throw new AssociationMultiplicityException("Cannot link '%s' with '%s'! Max limit '%s' hit".formatted(this, target, metadata.max()));

        target.link(this, metadata.id(), --callCounter);
        links.put(key, target);
    }

    public void unlink(AssociatedObject target, String id) throws AssociationsDoNotExistException, AssociationMultiplicityException {
        unlink(target, id, 2);
    }

    public void unlink(AssociatedObject target) throws AssociationsDoNotExistException, AssociationMultiplicityException {
        unlink(target, DEFAULT_ASSOCIATION_ID, 2);
    }

    private void unlink(AssociatedObject target, String id, int callCounter) throws AssociationsDoNotExistException, AssociationMultiplicityException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), target.getClass(), id);
        Map<Object, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), target.getClass(), id));

        var key = findQualifier(metadata, target).orElse(target);
        if (!links.containsKey(key))
            throw new AssociationsDoNotExistException(this.getClass(), target.getClass(), id);
        if (links.size() == metadata.min())
            throw new AssociationMultiplicityException("Cannot unlink '%s' with '%s'! Min limit '%s' hit".formatted(this, target, metadata.min()));

        target.unlink(this, metadata.id(), --callCounter);
        links.remove(key);
    }

    public List<AssociatedObject> getLinks(Class<?> targetType) throws AssociationsDoNotExistException {
        return getLinks(targetType, DEFAULT_ASSOCIATION_ID);
    }

    public List<AssociatedObject> getLinks(Class<?> targetType, String id) throws AssociationsDoNotExistException {
        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), targetType, id);
        Map<Object, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), targetType, id));
        return new ArrayList<>(links.values());
    }

    public Optional<AssociatedObject> findByQualifier(Class<?> targetType, Object qualifier) throws AssociationsDoNotExistException, AssociationIsNotQualifiedException {
        return findByQualifier(targetType, DEFAULT_ASSOCIATION_ID, qualifier);
    }

    public Optional<AssociatedObject> findByQualifier(Class<?> targetType, String id, Object qualifier) throws AssociationsDoNotExistException, AssociationIsNotQualifiedException {
        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), targetType, id);
        if (metadata.qualifier().isEmpty())
            throw new AssociationIsNotQualifiedException();

        Map<Object, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), targetType, id));
        return Optional.ofNullable(links.get(qualifier));
    }

    private Optional<Object> findQualifier(AssociationMetadata metadata, AssociatedObject target) {
        return metadata.qualifier()
                .flatMap(QualifierMetadata::qualifierField)
                .map(f -> {
                    try {
                        return f.get(target);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Field '%s' should be accessible!".formatted(f));
                    }
                });
    }

    public void printAssociations() {
        System.out.printf("Printing %s associations%n", this);
        associations.forEach((k,v) -> {
            System.out.println("metadata " + k);
            System.out.println("associations" + v);
            System.out.println();
        });
    }
}
