package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.AmbiguousAssociationAnnotationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.AssociationAnnotationNotFoundException;
import pl.edu.pjatk.mas.mp02.model.association.exception.IncorrectMultiplicitiesAnnotationDeclarationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AssociationsMetadataCacheHolder {
    private static final Map<Class<?>, List<AssociationMetadata>> cache = new ConcurrentHashMap<>();

    static AssociationMetadata resolveByTargetAndIdentifier(Class<?> thisType, Class<?> targetType, String identifier) {
        return Optional.ofNullable(cache.get(thisType))
                .orElseGet(() -> {
                    List<AssociationMetadata> thisMetadata = getAssociationsForType(thisType);
                    List<AssociationMetadata> targetMetadata = getAssociationsForType(targetType);

                    validateDuplicatedDeclarations(thisMetadata, thisType);
                    validateDuplicatedDeclarations(targetMetadata, targetType);
                    validateMultiplicities(thisMetadata, thisType);
                    validateMultiplicities(targetMetadata, targetType);
                    validateAmbiguousDeclarations(thisMetadata, targetMetadata, thisType);

                    cache.put(thisType, thisMetadata);
                    cache.put(targetType, targetMetadata);
                    return thisMetadata;
                })
                .stream()
                .filter(amd -> Objects.equals(targetType, amd.targetType()) && Objects.equals(identifier, amd.id()))
                .findFirst()
                .orElseThrow(() -> new AssociationAnnotationNotFoundException(targetType, identifier, thisType));
    }

    private static List<AssociationMetadata> getAssociationsForType(Class<?> type) {
        return Arrays.stream(type.getAnnotationsByType(Association.class))
                .map(AssociationMetadata::map)
                .toList();
    }

    private static void validateMultiplicities(List<AssociationMetadata> annotations, Class<?> type) {
        annotations.stream()
                .filter(a -> a.max() <= 0 || a.min() > a.max())
                .findAny()
                .ifPresent(a -> {
                    throw new IncorrectMultiplicitiesAnnotationDeclarationException(type);
                });
    }

    private static void validateDuplicatedDeclarations(List<AssociationMetadata> annotations, Class<?> type) {
        annotations.stream()
                .filter(a -> Collections.frequency(annotations, a) > 1)
                .findAny()
                .ifPresent(a -> {
                    throw new AmbiguousAssociationAnnotationException(type, a.targetType(), a.id());
                });
    }

    private static void validateAmbiguousDeclarations(List<AssociationMetadata> thisMetadata, List<AssociationMetadata> targetMetadata, Class<?> thisType) {
        thisMetadata.forEach(thisAssociation -> {
            var matchedAssociations = targetMetadata.stream()
                    .filter(targetAssociation -> Objects.equals(targetAssociation.targetType(), thisType) && Objects.equals(targetAssociation.id(), thisAssociation.id()))
                    .toList();
            if (matchedAssociations.size() > 1)
                throw new AmbiguousAssociationAnnotationException(thisType, thisAssociation.targetType(), thisAssociation.id());
        });
    }
}
