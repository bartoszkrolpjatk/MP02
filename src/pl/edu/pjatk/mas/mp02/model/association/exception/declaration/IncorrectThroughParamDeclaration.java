package pl.edu.pjatk.mas.mp02.model.association.exception.declaration;

public class IncorrectThroughParamDeclaration extends RuntimeException {
    public IncorrectThroughParamDeclaration(Class<?> throughType, Class<?> thisType, Class<?> targetType) {
        super("Missing through parameter '%s' for '%s' targeting '%s'".formatted(throughType.getSimpleName(), thisType.getSimpleName(), targetType.getSimpleName()));
    }

    public IncorrectThroughParamDeclaration(Class<?> thisType) {
        super("Through cannot equal target or class upon its declaration! For '%s'".formatted(thisType.getSimpleName()));
    }

    public IncorrectThroughParamDeclaration(Class<?> throughType, Class<?> thisType) {
        super("Incorrect Association declaration on '%s' trying to target '%s'. Correct declaration sets min and max params to 1".formatted(throughType, thisType));
    }
}
