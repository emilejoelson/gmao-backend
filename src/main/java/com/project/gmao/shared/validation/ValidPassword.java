package com.project.gmao.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Le mot de passe doit contenir au moins 6 caractères";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
