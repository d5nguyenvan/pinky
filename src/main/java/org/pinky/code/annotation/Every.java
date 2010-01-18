package org.pinky.code.annotation;

import net.sf.oval.configuration.annotation.Constraint;
import org.pinky.code.validator.DropDownValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Every {
    String value();
}

