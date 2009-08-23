package org.pinky.code.annotation.form;

import org.hibernate.validator.ValidatorClass;
import org.pinky.code.extension.validator.RadioButtonValidator;

import java.lang.annotation.*;

@Documented
@ValidatorClass(RadioButtonValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RadioButton {
}
 