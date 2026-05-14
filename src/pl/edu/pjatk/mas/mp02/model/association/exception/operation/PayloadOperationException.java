package pl.edu.pjatk.mas.mp02.model.association.exception.operation;

public class PayloadOperationException extends AssociationException {
    public PayloadOperationException(Class<?> noThroughParamType, Class<?> targetType) {
        super("No through param found on '%s' for target '%s'".formatted(noThroughParamType.getSimpleName(), targetType.getSimpleName()));
    }

    public PayloadOperationException(Class<?> expectedThroughType, Class<?> actualThroughType, Class<?> scannedClassType) {
        super("Expected '%s' through param, but '%s' expected on '%s'".formatted(expectedThroughType.getSimpleName(), actualThroughType.getSimpleName(), scannedClassType.getSimpleName()));
    }

    public PayloadOperationException(String message) {
        super(message);
    }
}
