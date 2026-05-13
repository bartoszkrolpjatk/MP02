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
    String id() default AssociatedObject.DEFAULT_ASSOCIATION_ID;
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    boolean isComposition() default false;
    Qualifier qualifier() default @Qualifier(fieldName = "", type = Void.class);
}
