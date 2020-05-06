package net.thumbtack.school.forums.model.validator;

import net.thumbtack.school.forums.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.length() > applicationProperties.getMinPasswordLength();
    }
}
