package org.pinky.annotation.form;


import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.*;

import org.pinky.validator.RadioButtonValidator;

@Constraint(checkWith = RadioButtonValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RadioButton {
}
 