package org.pinky.code.annotation.form;

import org.hibernate.validator.ValidatorClass;

import java.lang.annotation.*;
import org.pinky.code.extension.validator.CheckBoxValidator;
@Documented
@ValidatorClass(CheckBoxValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBox {
    boolean multi() default false;
}
