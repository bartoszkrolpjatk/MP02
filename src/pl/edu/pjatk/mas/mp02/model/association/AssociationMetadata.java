package pl.edu.pjatk.mas.mp02.model.association;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
class AssociationMetadata {
    private final Class<?> targetType;
    private final String name;

    public AssociationMetadata(Class<?> targetType, String name) {
        this.targetType = targetType;
        this.name = name;
    }
}
