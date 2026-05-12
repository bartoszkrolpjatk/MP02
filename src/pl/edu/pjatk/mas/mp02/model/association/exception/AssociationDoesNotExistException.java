package pl.edu.pjatk.mas.mp02.model.association.exception;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;

public class AssociationDoesNotExistException extends Exception {
    public AssociationDoesNotExistException(AssociatedObject target, String id) {
        super("Association(s) does not exist for target '%s' and id '%s'"
                .formatted(target, id));
    }
}
