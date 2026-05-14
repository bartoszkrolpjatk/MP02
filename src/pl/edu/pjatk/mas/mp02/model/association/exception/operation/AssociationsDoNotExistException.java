package pl.edu.pjatk.mas.mp02.model.association.exception.operation;

public class AssociationsDoNotExistException extends AssociationException {
    public AssociationsDoNotExistException(Class<?> thisType, Class<?> targetType, String id) {
        super("Associations do not exist between '%s' and '%s', id: '%s'"
                .formatted(thisType.getSimpleName(), targetType.getSimpleName(), id));
    }
}
