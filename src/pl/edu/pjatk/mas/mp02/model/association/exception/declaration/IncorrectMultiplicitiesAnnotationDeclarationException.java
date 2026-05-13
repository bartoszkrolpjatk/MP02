package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

public class IncorrectMultiplicitiesAnnotationDeclarationException extends RuntimeException {
    public IncorrectMultiplicitiesAnnotationDeclarationException(Class<?> incorrectlyDeclaredClass) {
        super("Min and max params declared incorrectly for '%s'! Max should be positive and greater than min.".formatted(incorrectlyDeclaredClass.getSimpleName()));
    }
}
