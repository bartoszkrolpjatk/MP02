package pl.edu.pjatk.mas.mp02.model.association.exception;

public class AmbiguousAssociationException extends RuntimeException {
    public AmbiguousAssociationException(Class<?> thisType, Class<?> targetType, String id) {
        super("Found duplicated associations on class '%s' with targetType: '%s' and id: '%s'".formatted(thisType.getSimpleName(), targetType.getSimpleName(), id));
    }

    public AmbiguousAssociationException(Class<?> thisType) {
        super("Found duplicated associations on class '%s'".formatted(thisType.getSimpleName()));
    }
}
