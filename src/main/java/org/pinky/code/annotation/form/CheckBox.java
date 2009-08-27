package org.pinky.code.annotation.form;

import org.pinky.code.validator.CheckBoxValidator;
import net.sf.oval.configuration.annotation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Constraint (checkWith = CheckBoxValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBox {
    boolean multi() default false;
    String message() default "must have at least one selected item";
}
