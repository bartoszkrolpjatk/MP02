package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AmbiguousAssociationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AssociationsMetadataCacheHolder {
    private static final Map<Class<?>, List<AssociationMetadata>> cache = new ConcurrentHashMap<>();

    static AssociationMetadata resolveByTargetOrIdentifier(Class<?> thisType, Class<?> targetType, String identifier) {
        List<AssociationMetadata> typeMetadata = cache.computeIfAbsent(thisType, AssociationsMetadataCacheHolder::getAssociationsForType);
        List<AssociationMetadata> targetMetadata = cache.computeIfAbsent(targetType, AssociationsMetadataCacheHolder::getAssociationsForType);
        validateAssociations(typeMetadata, targetMetadata, thisType);
        return findByTargetTypeOrIdentifier(typeMetadata, targetType, identifier)
                .orElseThrow(() -> new AssociationNotFoundException(targetType, identifier, thisType));
    }

    private static Optional<AssociationMetadata> findByTargetTypeOrIdentifier(List<AssociationMetadata> metadata, Class<?> targetType, String identifier) {
        return metadata.stream()
                .filter(amd -> Objects.equals(targetType, amd.targetType()))
                .filter(amd -> Objects.equals(identifier, amd.id()))
                .findFirst();
    }

    private static List<AssociationMetadata> getAssociationsForType(Class<?> type) {
        var annotations = Arrays.stream(type.getAnnotationsByType(Association.class))
                .map(AssociationMetadata::map)
                .toList();
        annotations.stream()
                .filter(a -> Collections.frequency(annotations, a) > 1)
                .findAny()
                .ifPresent(a -> {
                    throw new AmbiguousAssociationException(type, a.targetType(), a.id());
                });
        return annotations;
    }

    private static void validateAssociations(List<AssociationMetadata> thisMetadata, List<AssociationMetadata> targetMetadata, Class<?> thisType) {
        thisMetadata.forEach(thisAssociation -> {
            var matchedAssociations = targetMetadata.stream()
                    .filter(targetAssociation -> Objects.equals(targetAssociation.targetType(), thisType) && Objects.equals(targetAssociation.id(), thisAssociation.id()))
                    .toList();
            if (matchedAssociations.size() > 1)
                throw new AmbiguousAssociationException(thisType, thisAssociation.targetType(), thisAssociation.id());
        });
    }
}
