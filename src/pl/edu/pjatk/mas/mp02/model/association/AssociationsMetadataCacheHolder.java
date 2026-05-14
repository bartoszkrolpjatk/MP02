package pl.edu.pjatk.mas.mp02.model.association;

import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.AmbiguousAssociationAnnotationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.AssociationAnnotationNotFoundException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectCompositionUsageException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectMultiplicitiesAnnotationDeclarationException;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectQualifierDeclaration;
import pl.edu.pjatk.mas.mp02.model.association.exception.declaration.IncorrectThroughParamDeclaration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class AssociationsMetadataCacheHolder {
    private static final Map<Class<? extends AssociatedObject>, List<AssociationMetadata>> cache = new ConcurrentHashMap<>();

    static AssociationMetadata resolveByTargetAndId(Class<? extends AssociatedObject> thisType, Class<? extends AssociatedObject> targetType, String identifier) {
        return cache.computeIfAbsent(thisType, t -> {
                    List<AssociationMetadata> declaredAssociations = getAssociationsForType(t);
                    validateDuplicatedDeclarations(declaredAssociations, t);
                    validateMultiplicities(declaredAssociations, t);
                    validateComposition(declaredAssociations, t);
                    validateQualifierAndSetQualifierField(declaredAssociations, t);
                    validateThroughUsage(declaredAssociations, t);
                    return declaredAssociations;
                })
                .stream()
                .filter(amd -> Objects.equals(targetType, amd.targetType()) && Objects.equals(identifier, amd.id()))
                .findFirst()
                .orElseThrow(() -> new AssociationAnnotationNotFoundException(targetType, identifier, thisType));
    }

    private static List<AssociationMetadata> getAssociationsForType(Class<? extends AssociatedObject> type) {
        return Arrays.stream(type.getAnnotationsByType(Association.class))
                .map(AssociationMetadata::map)
                .toList();
    }

    private static void validateThroughUsage(List<AssociationMetadata> declaredAssociations, Class<?> thisType) {
        declaredAssociations.forEach(association -> association.payloadType()
                .ifPresent(payloadType -> {
                    if (payloadType.equals(thisType))
                        throw new IncorrectThroughParamDeclaration(thisType);

                    List<AssociationMetadata> targetAssociations = getAssociationsForType(association.targetType());
                    boolean correctThroughExistsOnTarget = targetAssociations.stream()
                            .filter(targetAssociation -> targetAssociation.payloadType().isPresent())
                            .anyMatch(targetAssociation ->
                                    payloadType.equals(targetAssociation.payloadType().get()) &&
                                            targetAssociation.targetType().equals(thisType) &&
                                            targetAssociation.id().equals(association.id()));
                    if (!correctThroughExistsOnTarget)
                        throw new IncorrectThroughParamDeclaration(payloadType, thisType, association.targetType());
                }));
    }

    private static void validateQualifierAndSetQualifierField(List<AssociationMetadata> declaredAssociations, Class<?> type) {
        declaredAssociations.forEach(a -> a.qualifier()
                .ifPresent(qualifier -> {
                    try {
                        Field field = a.targetType().getDeclaredField(qualifier.fieldName());
                        if (field.getType() != qualifier.type())
                            throw new IncorrectQualifierDeclaration(qualifier.type(), field, type);
                        field.trySetAccessible();
                        qualifier.qualifierField(field);
                    } catch (NoSuchFieldException e) {
                        throw new IncorrectQualifierDeclaration(qualifier.fieldName(), type);
                    }
                }));
    }

    private static void validateComposition(List<AssociationMetadata> declaredAssociations, Class<?> type) {
        declaredAssociations.stream()
                .filter(a -> a.isComposition() && (a.min() != 1 || a.max() != 1))
                .findAny()
                .ifPresent(a -> {
                    throw new IncorrectCompositionUsageException(type);
                });
    }

    private static void validateMultiplicities(List<AssociationMetadata> declaredAssociations, Class<?> type) {
        declaredAssociations.stream()
                .filter(a -> a.max() <= 0 || a.min() > a.max())
                .findAny()
                .ifPresent(a -> {
                    throw new IncorrectMultiplicitiesAnnotationDeclarationException(type);
                });
    }

    private static void validateDuplicatedDeclarations(List<AssociationMetadata> declaredAssociations, Class<?> type) {
        Set<String> uniqueCombinations = new HashSet<>();
        for (AssociationMetadata a : declaredAssociations) {
            String uniqueCombination = a.targetType().getName() + ":" + a.id();
            if (!uniqueCombinations.add(uniqueCombination))
                throw new AmbiguousAssociationAnnotationException(type, a.targetType(), a.id());
        }
    }
}
