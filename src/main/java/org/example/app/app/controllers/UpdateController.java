package org.example.app.app.controllers;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.model.UpdateUserModel;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.CreateUpdateParams;

public class UpdateController {


    public ActionAnswer updateUser(DbConnectInit connection, User user, CreateUpdateParams createUpdateParams) {
        UpdateUserModel updateUserModel = new UpdateUserModel(user, createUpdateParams);
        return updateUserModel.updateUser(connection);

    }
}
