package net.thumbtack.school.forums.model.validator;

import net.thumbtack.school.forums.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameValid, String> {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name.trim().length() < applicationProperties.getMaxNameLength();
    }
}
