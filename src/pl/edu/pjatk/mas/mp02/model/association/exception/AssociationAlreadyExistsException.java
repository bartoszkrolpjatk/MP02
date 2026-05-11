package pl.edu.pjatk.mas.mp02.model.association.exception;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;

public class AssociationAlreadyExistsException extends Exception {
    public AssociationAlreadyExistsException(AssociatedObject thisObject, AssociatedObject targetObject) {
        super("Association already exists between %s and %s".formatted(thisObject, targetObject));
    }
}
