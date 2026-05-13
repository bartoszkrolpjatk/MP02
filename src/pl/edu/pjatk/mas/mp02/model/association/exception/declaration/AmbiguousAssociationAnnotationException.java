package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

public class AmbiguousAssociationAnnotationException extends RuntimeException {
    public AmbiguousAssociationAnnotationException(Class<?> thisType, Class<?> targetType, String id) {
        super("Found duplicated associations on class '%s' with targetType: '%s' and id: '%s'".formatted(thisType.getSimpleName(), targetType.getSimpleName(), id));
    }
}
