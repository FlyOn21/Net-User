package org.example.app.app.controllers;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.model.CreateUserModel;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.CreateUpdateParams;

public class CreateController {

    public ActionAnswer controllerCreateUser(DbConnectInit connect, CreateUpdateParams createParams) {
        CreateUserModel createUserModel = new CreateUserModel();
        return createUserModel.createUser(connect, createParams);
    }
}
