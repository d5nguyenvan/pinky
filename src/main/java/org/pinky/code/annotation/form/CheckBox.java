package org.pinky.code.annotation.form;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBox {
    boolean multi() default false;
}
