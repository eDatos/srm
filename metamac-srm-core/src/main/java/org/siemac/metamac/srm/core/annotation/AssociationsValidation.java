package org.siemac.metamac.srm.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ValidatorClass(AssociationsValidationValidator.class)
public @interface AssociationsValidation {

    String message() default "The collection cannot be empty";
}