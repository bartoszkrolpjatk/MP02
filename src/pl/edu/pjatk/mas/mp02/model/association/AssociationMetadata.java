package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
class AssociationMetadata {
    private final Class<?> targetType;
    private final String identifier;

    public AssociationMetadata(Class<?> targetType, String identifier) {
        this.targetType = targetType;
        this.identifier = identifier;
    }

    public static AssociationMetadata map(Association association) {
        return AssociationMetadata
                .builder()
                .identifier(association.identifier())
                .targetType(association.targetType())
                .build();
    }
}
