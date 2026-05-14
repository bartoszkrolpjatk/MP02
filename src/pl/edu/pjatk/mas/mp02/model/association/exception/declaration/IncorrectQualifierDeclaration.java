package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

import java.lang.reflect.Field;

public class IncorrectQualifierDeclaration extends RuntimeException {
    public IncorrectQualifierDeclaration(Class<?> declaredType, Field field, Class<?> thisType) {
        super("Qualifier type '%s' mismatch with field '%s' on class '%s'".formatted(declaredType, field, thisType));
    }

    public IncorrectQualifierDeclaration(String declaredField, Class<?> thisType) {
        super("Field '%s' does not exist on '%s'".formatted(declaredField, thisType));
    }

    public IncorrectQualifierDeclaration(Field field) {
        super("Can not access '%s'".formatted(field));
    }
}
