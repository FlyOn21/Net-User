package org.example.app.app.controllers;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.model.DeleteUserModel;
import org.example.app.app.utils.ActionAnswer;

public class DeleteController {
    public ActionAnswer deleteUser(DbConnectInit connection, User user) {
        DeleteUserModel deleteUserModel = new DeleteUserModel(user);
        return deleteUserModel.deleteUser(connection);
    }
}
