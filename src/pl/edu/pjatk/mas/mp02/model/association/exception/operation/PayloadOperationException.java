package pl.edu.pjatk.mas.mp02.model.association.exception.operation;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;

public class PayloadOperationException extends AssociationException {
    public PayloadOperationException(Class<?> noPayloadParamType, Class<?> targetType) {
        super("No payload param found on '%s' for target '%s'".formatted(noPayloadParamType.getSimpleName(), targetType.getSimpleName()));
    }

    public PayloadOperationException(Class<?> actualPayloadType, Class<?> expectedPayloadType, Class<?> scannedClassType) {
        super("Got '%s' 'payload' param, but '%s' expected on '%s'".formatted(actualPayloadType.getSimpleName(), expectedPayloadType.getSimpleName(), scannedClassType.getSimpleName()));
    }

    public PayloadOperationException(AssociatedObject thisObj, AssociatedObject target) {
        super("Unable to perform payload operation. Associations between '%s' and '%s' do not exist".formatted(thisObj, target));
    }

    public PayloadOperationException(String message) {
        super(message);
    }
}
