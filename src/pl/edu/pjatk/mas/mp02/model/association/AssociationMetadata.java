package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Builder;

@Builder
record AssociationMetadata(
        Class<?> targetType,
        String identifier,
        String targetIdentifier
) {
    public static AssociationMetadata map(Association association) {
        return AssociationMetadata
                .builder()
                .identifier(association.identifier())
                .targetType(association.targetType())
                .targetIdentifier(association.targetIdentifier())
                .build();
    }
}
