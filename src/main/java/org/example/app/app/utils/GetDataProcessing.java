package org.example.app.app.utils;

import org.example.app.app.controllers.ReadController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.exceptions.ConnectException;

import java.util.List;
import java.util.Optional;

public class GetDataProcessing {

    public static String getProcessedData(DbConnectInit connection, ReadParams readParams, ReadController readController, boolean scalar) {
        try {
            Optional<List<User>> result;
            if (scalar) {
                result = readController.controllerReadById(connection, readParams);
            } else {
                result = readController.controllerReadAll(connection, readParams);
            }
            StringBuilder responseString = new StringBuilder();
            if (result.isPresent()) {
                result.get().forEach(user -> responseString.append(user).append("\n"));
                return responseString.toString();
            } else {
                return "No users found";
            }
        } catch (ConnectException e) {
            return "Error: " + e.getMessage();
        }
    }
}
