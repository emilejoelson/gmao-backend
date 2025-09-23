package com.project.gmao.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.project.gmao.common.constants.SecurityConstants.MIN_PASSWORD_LENGTH;


public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
}
