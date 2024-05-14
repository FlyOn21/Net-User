package org.example.app.app.utils;

import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

public class ValidateUtils {

    public static String validateProcessing(ValidateAnswer answer) {
        if (!answer.isValid()) {
            StringBuilder validateErrorsString = new StringBuilder();
            answer.getErrorsList().forEach(
                    error -> validateErrorsString.append(error).append("\n")
            );
            return validateErrorsString.toString();
        }
        return "";
    }
}
