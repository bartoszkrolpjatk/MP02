package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AmbiguousAssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class AssociationsMetadataCacheHolder {
    private static final Map<Class<?>, List<AssociationMetadata>> cache = new HashMap<>();

    static AssociationMetadata resolveByIdentifier(Class<?> thisType, String identifier) {

    }

    static Optional<AssociationMetadata> resolveByTarget(Class<?> thisType, Class<?> targetType) {
        List<AssociationMetadata> typeMetadata = cache.computeIfAbsent(thisType, t -> new ArrayList<>());
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

    private static Optional<AssociationMetadata> findByTarget(Class<?> targetType) {
        List<AssociationMetadata> typeMetadata = cache.computeIfAbsent(thisType, t -> new ArrayList<>());
        return typeMetadata.stream()
                .filter(amd -> Objects.equals(amd.targetType(), targetType))
                .findFirst();
    }
}
