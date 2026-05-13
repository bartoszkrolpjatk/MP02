package pl.edu.pjatk.mas.mp02.model.association;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@Accessors(fluent = true)
class QualifierMetadata {
    private final String fieldName;
    private final Class<?> type;
    private Field qualifierField;

    public Optional<Field> qualifierField() {
        return Optional.ofNullable(qualifierField);
    }
}
