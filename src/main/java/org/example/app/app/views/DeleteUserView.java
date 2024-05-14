package org.example.app.app.views;

import org.example.app.app.controllers.DeleteController;
import org.example.app.app.controllers.ReadController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.ReadParams;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.List;
import java.util.Optional;

import static org.example.app.app.utils.ValidateUtils.validateProcessing;

public class DeleteUserView {
    private final DbConnectInit connection;

    public DeleteUserView(DbConnectInit connection) {
        this.connection = connection;
    }

    public String deleteUserViewProcessing(String idData) {
        String isValid;
        ReadParams readParamsById = new ReadParams();
        ValidateAnswer validateId = readParamsById.setId(idData);
        isValid = validateProcessing(validateId);
        if (!isValid.isEmpty()) {
            return isValid;
        }
        ReadController readController = new ReadController();
        Optional<List<User>> currentUser = readController.controllerReadById(connection, readParamsById);
        if (currentUser.isPresent()) {
            User user = currentUser.get().getFirst();
            DeleteController deleteController = new DeleteController();
            ActionAnswer deleteUserResult = deleteController.deleteUser(connection, user);
            return deleteUserResult.getSuccessMsg();
        } else {
            return "No users found";
        }
    }
}
