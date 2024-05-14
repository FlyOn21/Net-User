package org.example.app.app.views;

import org.example.app.app.controllers.CreateController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.CreateUpdateParams;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.Arrays;
import java.util.List;

import static org.example.app.app.utils.ValidateUtils.validateProcessing;

public class CreateUserView {
    private final DbConnectInit connection;

    public CreateUserView(DbConnectInit connection) {
        this.connection = connection;
    }

    public String createUserViewProcessing(String request) {
            String isValid;
            CreateUpdateParams createParams = new CreateUpdateParams();
            List<String> inputData = Arrays.stream(request.split(","))
                    .map(String::strip)
                    .toList();
            if (inputData.size() != 4) {
                return "Invalid number of parameters";
            }
            String firstName = inputData.get(0);
            ValidateAnswer validateFirstName = createParams.setFirstName(firstName);
            isValid = validateProcessing(validateFirstName);
            if (!isValid.isEmpty()) {
                return isValid;
            }
            String lastName = inputData.get(1);
            ValidateAnswer validateLastName = createParams.setLastName(lastName);
            isValid = validateProcessing(validateLastName);
            if (!isValid.isEmpty()) {
                return isValid;
            }
            String email = inputData.get(2);
            ValidateAnswer validateEmail = createParams.setEmail(email, connection, false);
            isValid = validateProcessing(validateEmail);
            if (!isValid.isEmpty()) {
                return isValid;
            }
            String phone = inputData.get(3);
            ValidateAnswer validatePhone = createParams.setPhone(phone);
            isValid = validateProcessing(validatePhone);
            if (!isValid.isEmpty()) {
                return isValid;
            }
            CreateController controllerCreateUser = new CreateController();
            ActionAnswer create = controllerCreateUser.controllerCreateUser(connection, createParams);
            if (create.isSuccess()) {
                return create.getSuccessMsg();
            } else {
                StringBuilder createErrorsString = new StringBuilder();
                create.getErrors().forEach(
                    error -> createErrorsString.append(error).append("\n")
                );
                return createErrorsString.toString();
            }
    }
}

