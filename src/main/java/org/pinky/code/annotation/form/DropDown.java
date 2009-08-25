package org.pinky.code.annotation.form;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDown {
    boolean multi() default false;
}