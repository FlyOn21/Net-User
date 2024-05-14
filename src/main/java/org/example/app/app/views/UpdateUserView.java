package org.example.app.app.views;

import org.example.app.app.controllers.ReadController;
import org.example.app.app.controllers.UpdateController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.CreateUpdateParams;
import org.example.app.app.utils.ReadParams;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.app.app.utils.ValidateUtils.validateProcessing;

public class UpdateUserView {
    private final DbConnectInit connection;

    public UpdateUserView(DbConnectInit connection) {
        this.connection = connection;
    }

    public String updateUserViewProcessing(String request) {
        String isValid;
        CreateUpdateParams updateParams = new CreateUpdateParams();
        ReadParams readParamsById = new ReadParams();
        List<String> inputData = Arrays.stream(request.split(","))
                .map(String::strip)
                .toList();
        if (inputData.size() != 5) {
            return "Invalid number of parameters";
        }
        String idData = inputData.get(0);
        ValidateAnswer validateId = readParamsById.setId(idData);
        isValid = validateProcessing(validateId);
        if (!isValid.isEmpty()) {
            return isValid;
        }
        ReadController readController = new ReadController();
        Optional<List<User>> currentUser = readController.controllerReadById(connection, readParamsById);

        if (currentUser.isPresent()) {
            User user = currentUser.get().getFirst();
            String firstName = inputData.get(1);
            if (firstName.isEmpty()) {
                updateParams.setFirstName(user.getFirstName());
            } else {
                ValidateAnswer validateFirstName = updateParams.setFirstName(firstName);
                isValid = validateProcessing(validateFirstName);
                if (!isValid.isEmpty()) {
                    return isValid;
                }
            }

            String lastName = inputData.get(2);
            if (lastName.isEmpty()) {
                updateParams.setLastName(user.getLastName());
            } else {
                ValidateAnswer validateLastName = updateParams.setLastName(lastName);
                isValid = validateProcessing(validateLastName);
                if (!isValid.isEmpty()) {
                    return isValid;
                }
            }

            String email = inputData.get(3);
            if (email.isEmpty()) {
                updateParams.setEmail(user.getEmail(), connection, true);
            } else {
                ValidateAnswer validateEmail = updateParams.setEmail(email, connection, false);
                isValid = validateProcessing(validateEmail);
                if (!isValid.isEmpty()) {
                    return isValid;
                }
            }

            String phone = inputData.get(4);
            if (phone.isEmpty()) {
                updateParams.setPhone(user.getPhone());
            } else {
                ValidateAnswer validatePhone = updateParams.setPhone(phone);
                isValid = validateProcessing(validatePhone);
                if (!isValid.isEmpty()) {
                    return isValid;
                }
            }

            UpdateController updateController = new UpdateController();
            ActionAnswer update = updateController.updateUser(connection, currentUser.get().getFirst(), updateParams);
            if (update.isSuccess()) {
                return update.getSuccessMsg();
            } else {
                StringBuilder updateErrorsString = new StringBuilder();
                update.getErrors().forEach(
                        error -> updateErrorsString.append(error).append("\n")
                );
                return updateErrorsString.toString();
            }
        } else {
            return "No users found";
        }
    }
}
