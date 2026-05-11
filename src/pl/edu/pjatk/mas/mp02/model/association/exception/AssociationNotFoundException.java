package pl.edu.pjatk.mas.mp02.model.association.exception;

import pl.edu.pjatk.mas.mp02.model.association.AssociatedObject;

import java.util.Objects;

public class AssociationNotFoundException extends RuntimeException {
    public AssociationNotFoundException(Class<?> targetType, String identifier, Class<?> thisType) {
        super("Associated annotation with targetType %s%s expected on %s class but not found!"
                .formatted(targetType,
                        Objects.equals(identifier, AssociatedObject.DEFAULT_IDENTIFIER) ? "" : " and identifier '" + identifier + "'",
                        thisType));
    }
}
