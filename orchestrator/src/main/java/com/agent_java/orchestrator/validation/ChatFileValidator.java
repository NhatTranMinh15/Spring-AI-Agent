package com.agent_java.orchestrator.validation;

import com.agent_java.orchestrator.utils.Constant;
import com.agent_java.orchestrator.viewmodel.ChatRequestVm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChatFileValidator implements ConstraintValidator<ValidChatFile, ChatRequestVm> {

    @Override
    public boolean isValid(ChatRequestVm value, ConstraintValidatorContext context) {
        if (value == null || value.files()== null) {
            return true;
        }
        var files = value.files();
        boolean isValid = true;
        if (files == null) {
            return false;
        }
        for (var file : files) {
            if (file.isEmpty()) {
                addViolation(context, "File must not be empty");
                isValid = false;
            }
            String contentType = file.getContentType();
            if (contentType != null && !contentType.startsWith("image/")) {
                addViolation(context, "Only image files are allowed");
                isValid = false;
            }
            if (file.getSize() > Constant.MAXIMUM_UPLOAD_FILE_SIZE) {
                addViolation(context, "File size must not exceed 5MB");
                isValid = false;
            }
        }
        return isValid;
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        if (context != null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("files")
                    .addConstraintViolation();
        }
    }

}
