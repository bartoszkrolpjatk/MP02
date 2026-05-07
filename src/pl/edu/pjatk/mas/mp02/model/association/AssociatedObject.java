package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAlreadyExistsException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AssociatedObject {
    private static final Map<Class<?>, List<AssociationMetadata>> associationsMetadata = new HashMap<>();

    private final Map<AssociationMetadata, Map<AssociatedObject, AssociatedObject>> associations = new HashMap<>();

    public void link(AssociatedObject target) throws AssociationAlreadyExistsException {
        link(target, 2);
    }

    private void link(AssociatedObject target, int callCounter) throws AssociationAlreadyExistsException {
        if (callCounter <=0 )
            return;

        AssociationMetadata metadata = getAssociationMetadata(this.getClass(), target.getClass());
        Map<AssociatedObject, AssociatedObject> links = associations.computeIfAbsent(metadata, amd -> new HashMap<>());

        if (links.containsKey(target))
            throw new AssociationAlreadyExistsException(this, target);

        target.link(this, --callCounter);
        links.put(target, target);
    }

    private static AssociationMetadata getAssociationMetadata(Class<?> thisType, Class<?> targetType) {
        List<AssociationMetadata> typeMetadata = associationsMetadata.computeIfAbsent(thisType, t -> new ArrayList<>());
        return typeMetadata.stream()
                .filter(amd -> Objects.equals(amd.targetType(), targetType))
                .findFirst()
                .orElseGet(() -> {
                    AssociationMetadata toAdd = Arrays.stream(thisType.getAnnotationsByType(Association.class))
                            .filter(a -> Objects.equals(a.targetType(), targetType))
                            .findFirst()
                            .map(a -> AssociationMetadata
                                    .builder()
                                    .name(a.name())
                                    .targetType(a.targetType())
                                    .build())
                            .orElseThrow(() -> new AssociationNotFoundException(targetType, thisType));
                    typeMetadata.add(toAdd);
                    return toAdd;
                });
    }

    public void printAssociations() {
        System.out.println(associations);
    }
}
