package com.project.gmao.shared.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Email(message = "Format d'email invalide")
@Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
@Documented
public @interface ValidEmail {
    String message() default "Email invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
