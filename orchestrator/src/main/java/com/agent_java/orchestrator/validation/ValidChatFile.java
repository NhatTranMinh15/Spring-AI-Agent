package com.agent_java.orchestrator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ChatFileValidator.class})
public @interface ValidChatFile {

    String message() default "Invalid files";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
