package org.example.app.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.exceptions.ConnectException;
import org.example.app.app.repository.UserRepository;

import java.util.List;

public class DBController {
    private static final Logger DB_INIT_CONTROLLER_LOGGER =
            LogManager.getLogger(UserRepository.class);
    public DbConnectInit connect() {
        DbConnectInit connectInit = new DbConnectInit();
        if(connectInit.isConnected()) {
            return connectInit;
        }
        else {
            List<String> errors = connectInit.getConnectErrors();
            StringBuilder errorString = new StringBuilder();
            for (String error : errors) {
                errorString.append(error).append("\n");
            }
            DB_INIT_CONTROLLER_LOGGER.error(errorString.toString());
            throw new ConnectException(errorString.toString());
        }
    }
}
