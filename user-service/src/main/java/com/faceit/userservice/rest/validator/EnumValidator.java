package com.faceit.userservice.rest.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ReportAsSingleViolation
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClazz();

    String message() default "Value can not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
