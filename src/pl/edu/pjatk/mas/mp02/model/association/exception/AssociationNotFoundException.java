package pl.edu.pjatk.mas.mp02.model.association.exception;

public class AssociationNotFoundException extends RuntimeException {
    public AssociationNotFoundException(Class<?> targetType, Class<?> thisType) {
        super("Associated annotation with targetType %s expected on %s class but not found!".formatted(targetType, thisType));
    }
}
