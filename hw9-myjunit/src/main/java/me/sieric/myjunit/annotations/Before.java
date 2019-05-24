package me.sieric.myjunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation for method which will be run before each test */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
}