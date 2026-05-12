package pl.edu.pjatk.mas.mp02.model.association.exception;

public class AssociationAnnotationNotFoundException extends RuntimeException {
    public AssociationAnnotationNotFoundException(Class<?> targetType, String id, Class<?> thisType) {
        super("@Associated with targetType='%s' and id='%s' expected on %s class but not found!"
                .formatted(targetType.getSimpleName(), id, thisType.getSimpleName()));
    }
}
