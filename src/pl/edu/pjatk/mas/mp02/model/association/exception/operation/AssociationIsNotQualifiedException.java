package pl.edu.pjatk.mas.mp02.model.association.exception.operation;

public class AssociationIsNotQualifiedException extends AssociationException {
    public AssociationIsNotQualifiedException(Class<?> targetType, String id) {
        super("Cannot perform find by qualifier when association for target '%s' and id '%s' is not qualified".formatted(targetType.getSimpleName(), id));
    }
}
