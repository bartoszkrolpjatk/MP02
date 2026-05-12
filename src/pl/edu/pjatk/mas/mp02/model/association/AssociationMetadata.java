package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@RequiredArgsConstructor
@Accessors(fluent = true)
class AssociationMetadata {
    private final Class<?> targetType;
    private final String id;
    private final int min;
    private final int max;

    public static AssociationMetadata map(Association association) {
        return AssociationMetadata
                .builder()
                .id(association.id())
                .targetType(association.targetType())
                .min(association.min())
                .max(association.max())
                .build();
    }
}
