package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationDoesNotExistException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static pl.edu.pjatk.mas.mp02.model.association.AssociationsMetadataCacheHolder.resolveByTargetAndIdentifier;

public abstract class AssociatedObject {
    public static final String DEFAULT_ASSOCIATION_ID = "";

    private final Map<AssociationMetadata, Map<AssociatedObject, AssociatedObject>> associations = new HashMap<>();

    public void link(AssociatedObject target, String id) throws AssociationAlreadyExistsException {
        link(target, id, 2);
    }

    public void link(AssociatedObject target) throws AssociationAlreadyExistsException {
        link(target, DEFAULT_ASSOCIATION_ID,2);
    }

    private void link(AssociatedObject target, String id, int callCounter) throws AssociationAlreadyExistsException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), target.getClass(), id);
        Map<AssociatedObject, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new HashMap<>());

        if (links.containsKey(target))
            throw new AssociationAlreadyExistsException(this, target);

        target.link(this, metadata.id(), --callCounter);
        links.put(target, target);
    }

    public void unlink(AssociatedObject target, String id) throws AssociationDoesNotExistException {
        unlink(target, id, 2);
    }

    public void unlink(AssociatedObject target) throws AssociationDoesNotExistException {
        unlink(target, DEFAULT_ASSOCIATION_ID, 2);
    }

    private void unlink(AssociatedObject target, String id, int callCounter) throws AssociationDoesNotExistException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetAndIdentifier(this.getClass(), target.getClass(), id);
        Map<AssociatedObject, AssociatedObject> links = Optional.ofNullable(associations.get(metadata))
                .orElseThrow(() -> new AssociationDoesNotExistException(target, id));
        if (!links.containsKey(target))
            throw new AssociationDoesNotExistException(target, id);

        target.unlink(this, metadata.id(), --callCounter);
        links.remove(target);
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
