package com.coding.car_rental_app.validation.imgcustomvalidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) //this annotation is applied to a field
@Retention(RetentionPolicy.RUNTIME) //applied at runtime
@Constraint(validatedBy = ImgFileValidator.class) //validator
public @interface ImgValidation {

    String message() default "Invalid image file";
    Class<?>[] groups() default {}; //for validation groups

    Class<? extends Payload>[] payload() default {}; //payload for metadata
}
