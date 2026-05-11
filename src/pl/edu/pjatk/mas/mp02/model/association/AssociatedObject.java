package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

import static pl.edu.pjatk.mas.mp02.model.association.AssociationsMetadataCacheHolder.resolveByTargetOrIdentifier;

public abstract class AssociatedObject {
    public static final String DEFAULT_IDENTIFIER = "";

    private final Map<AssociationMetadata, Map<AssociatedObject, AssociatedObject>> associations = new HashMap<>();

    public void link(AssociatedObject target, String identifier) throws AssociationAlreadyExistsException {
        link(target, identifier, 2);
    }

    public void link(AssociatedObject target) throws AssociationAlreadyExistsException {
        link(target, DEFAULT_IDENTIFIER,2);
    }

    private void link(AssociatedObject target, String identifier, int callCounter) throws AssociationAlreadyExistsException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = resolveByTargetOrIdentifier(this.getClass(), target.getClass(), identifier);
        Map<AssociatedObject, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new HashMap<>());

        if (links.containsKey(target))
            throw new AssociationAlreadyExistsException(this, target);

        target.link(this, metadata.targetIdentifier(),--callCounter);
        links.put(target, target);
    }

    public void printAssociations() {
        associations.forEach((k,v) -> {
            System.out.println(k);
            System.out.println(v);
            System.out.println();
        });
    }
}
