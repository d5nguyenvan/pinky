package org.pinky.code.annotation.form;


import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.*;

import org.pinky.code.validator.DropDownValidator;

@Constraint(checkWith = DropDownValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDown {
    boolean multi() default false;
}