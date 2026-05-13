package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationMultiplicityException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationsDoNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static pl.edu.pjatk.mas.mp02.model.association.AssociationsMetadataCacheHolder.resolveByTargetAndIdentifier;

public abstract class AssociatedObject {
    public static final String DEFAULT_ASSOCIATION_ID = "";

    private final Map<AssociationMetadata, Map<AssociatedObject, AssociatedObject>> associations = new ConcurrentHashMap<>();

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
        Map<AssociatedObject, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new ConcurrentHashMap<>());

        if (links.containsKey(target))
            throw new AssociationAlreadyExistsException(this, target);
        if (links.size() == metadata.max())
            throw new AssociationMultiplicityException("Cannot link '%s' with '%s'! Max limit '%s' hit".formatted(this, target, metadata.max()));

        target.link(this, metadata.id(), --callCounter);
        links.put(target, target);
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
        Map<AssociatedObject, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), target.getClass(), id));

        if (!links.containsKey(target))
            throw new AssociationsDoNotExistException(this.getClass(), target.getClass(), id);
        if (links.size() == metadata.min())
            throw new AssociationMultiplicityException("Cannot unlink '%s' with '%s'! Min limit '%s' hit".formatted(this, target, metadata.min()));

        target.unlink(this, metadata.id(), --callCounter);
        links.remove(target);
    }

    public List<AssociatedObject> getLinks(Class<?> targetType) throws AssociationsDoNotExistException {
        return getLinks(targetType, DEFAULT_ASSOCIATION_ID);
    }

    public List<AssociatedObject> getLinks(Class<?> targetType, String id) throws AssociationsDoNotExistException {
        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), targetType, id);
        Map<AssociatedObject, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationsDoNotExistException(this.getClass(), targetType, id));
        return new ArrayList<>(links.values());
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
