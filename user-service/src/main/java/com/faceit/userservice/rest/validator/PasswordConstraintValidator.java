package com.faceit.userservice.rest.validator;

import com.faceit.userservice.config.PasswordProperties;
import lombok.AllArgsConstructor;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@AllArgsConstructor
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private final PasswordProperties properties;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                //Length rule. Min 10 max 128 characters
                new LengthRule(properties.getMinLength(), properties.getMaxLength()),
                // At least one upper case letter
                new CharacterRule(EnglishCharacterData.UpperCase, properties.getUpperCase()),
                //At least one lower case letter
                new CharacterRule(EnglishCharacterData.LowerCase, properties.getLowerCase()),
                //At least one number
                new CharacterRule(EnglishCharacterData.Digit, properties.getDigit()),
                //At least one special characters
                new CharacterRule(EnglishCharacterData.Special, properties.getSpecial()),
                new WhitespaceRule()
        ));

        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;

        }
        //Sending one message each time failed validation.
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                        passwordValidator.getMessages(result).stream().findFirst().get())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
