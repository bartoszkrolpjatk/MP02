package pl.edu.pjatk.mas.mp02.model.association;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Associations.class)
public @interface Association {
    Class<?> targetType();
    String id() default AssociatedObject.DEFAULT_IDENTIFIER;
}
