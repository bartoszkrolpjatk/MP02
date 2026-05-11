package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AmbiguousAssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static pl.edu.pjatk.mas.mp02.model.association.AssociatedObject.DEFAULT_IDENTIFIER;

class AssociationsMetadataCacheHolder {
    private static final Map<Class<?>, List<AssociationMetadata>> cache = new HashMap<>();

    static AssociationMetadata resolveByTargetOrIdentifier(Class<?> thisType, Class<?> targetType, String identifier) {
        List<AssociationMetadata> typeMetadata = cache.computeIfAbsent(thisType, t -> new ArrayList<>());
        return findByTargetTypeOrIdentifier(typeMetadata, targetType, identifier)
                .orElseGet(() -> {
                    addToCache(typeMetadata, thisType);
                    return findByTargetTypeOrIdentifier(typeMetadata, targetType, identifier)
                            .orElseThrow(() -> new AssociationNotFoundException(targetType, identifier, thisType));
                });
    }

    private static Optional<AssociationMetadata> findByTargetTypeOrIdentifier(List<AssociationMetadata> metadata, Class<?> targetType, String identifier) {
        var byTarget = metadata.stream()
                .filter(amd -> Objects.equals(targetType, amd.targetType()))
                .toList();
        if (byTarget.size() == 1)
            return Optional.of(byTarget.getFirst());
        else if (byTarget.size() > 1) {
            var byIdentifier = byTarget.stream()
                    .filter(amd -> Objects.equals(identifier, DEFAULT_IDENTIFIER) || Objects.equals(identifier, amd.identifier()))
                    .toList();
            if (byIdentifier.size() == 1)
                return Optional.of(byIdentifier.getFirst());
            else if (byIdentifier.size() > 1)
                throw new AmbiguousAssociationException("Found multiple associations for target: %s".formatted(targetType));
        }
        return Optional.empty();
    }

    private static void addToCache(List<AssociationMetadata> metadata, Class<?> thisType) {
        for (var a : thisType.getAnnotationsByType(Association.class))
            metadata.add(AssociationMetadata.map(a));
    }
}
