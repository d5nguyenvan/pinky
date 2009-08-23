package org.pinky.code.annotation.form;

import org.hibernate.validator.ValidatorClass;
import org.pinky.code.extension.validator.DropDownValidator;

import java.lang.annotation.*;

@Documented
@ValidatorClass(DropDownValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDown {
    boolean multi() default false;
}