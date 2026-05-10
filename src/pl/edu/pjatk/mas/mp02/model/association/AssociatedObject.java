package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AmbiguousAssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class AssociatedObject {
    private static final Map<Class<?>, List<AssociationMetadata>> associationsMetadata = new HashMap<>();

    private final Map<AssociationMetadata, Map<AssociatedObject, AssociatedObject>> associations = new HashMap<>();

    public void link(AssociatedObject target, String identifier) throws AssociationAlreadyExistsException {
        link(target, identifier, 2);
    }

    public void link(AssociatedObject target) throws AssociationAlreadyExistsException {
        link(target, null,2);
    }

    private void link(AssociatedObject target, String identifier, int callCounter) throws AssociationAlreadyExistsException {
        if (callCounter <= 0)
            return;

        AssociationMetadata metadata = getOrCreateMetadata(this.getClass(), target.getClass());
        Map<AssociatedObject, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new HashMap<>());

        if (links.containsKey(target))
            throw new AssociationAlreadyExistsException(this, target);

        target.link(this, --callCounter);
        links.put(target, target);
    }

    private static AssociationMetadata getOrCreateMetadata(Class<?> thisType, Class<?> targetType, Predicate<Association> associationPredicate) {
        List<AssociationMetadata> typeMetadata = associationsMetadata.computeIfAbsent(thisType, t -> new ArrayList<>());
        return typeMetadata.stream()
                .filter(amd -> Objects.equals(amd.targetType(), targetType))
                .findFirst()
                .orElseGet(() -> {
                    var annotations = Arrays.stream(thisType.getAnnotationsByType(Association.class))
                            .filter(associationPredicate)
                            .toList();
                    if (annotations.size() > 1)
                        throw new AmbiguousAssociationException("Found multiple associations between this: %s and target: %s.".formatted(thisType, targetType));
                    AssociationMetadata toAdd = annotations.stream()
                            .findFirst()
                            .map(AssociationMetadata::map)
                            .orElseThrow(() -> new AssociationNotFoundException(targetType, thisType));
                    typeMetadata.add(toAdd);
                    return toAdd;
                });
    }

    public void printAssociations() {
        System.out.println(associations);
    }
}
