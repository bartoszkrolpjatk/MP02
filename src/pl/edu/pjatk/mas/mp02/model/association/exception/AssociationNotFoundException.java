package pl.edu.pjatk.mas.mp02.model.association.exception;

public class AssociationNotFoundException extends RuntimeException {
    public AssociationNotFoundException(Class<?> targetType, String identifier, Class<?> thisType) {
        super("Associated annotation with targetType='%s' and id='%s' expected on %s class but not found!"
                .formatted(targetType.getSimpleName(), identifier, thisType.getSimpleName()));
    }
}
