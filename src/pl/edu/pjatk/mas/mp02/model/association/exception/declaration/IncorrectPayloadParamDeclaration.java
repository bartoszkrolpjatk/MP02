package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

public class IncorrectPayloadParamDeclaration extends RuntimeException {
    public IncorrectPayloadParamDeclaration(Class<?> payloadType, Class<?> thisType, Class<?> targetType) {
        super("Missing payload parameter '%s' on '%s' and targeting '%s'".formatted(payloadType.getSimpleName(), targetType.getSimpleName(), thisType.getSimpleName()));
    }

    public IncorrectPayloadParamDeclaration(Class<?> thisType) {
        super("Payload cannot equal target or class upon its declaration! For '%s'".formatted(thisType.getSimpleName()));
    }

}
