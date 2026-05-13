package pl.edu.pjatk.mas.mp02.model.association.exception.operation;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;

public class AssociationAlreadyExistsException extends Exception {
    public AssociationAlreadyExistsException(AssociatedObject thisObject, AssociatedObject targetObject, Object key) {
        super("Association already exists between %s and %s for key '%s'".formatted(thisObject, targetObject, key));
    }
}
