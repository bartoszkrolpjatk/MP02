package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

public class IncorrectCompositionUsageException extends RuntimeException {
    public IncorrectCompositionUsageException(Class<?> type) {
        super("Incorrect composition usage on '%s'! If composition enabled min and max must equal 1".formatted(type.getSimpleName()));
    }
}
