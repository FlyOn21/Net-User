package org.example.app.app.views;

import org.example.app.app.controllers.ReadController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.utils.ReadParams;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;


import static org.example.app.app.utils.GetDataProcessing.getProcessedData;
import static org.example.app.app.utils.ValidateUtils.validateProcessing;

public class GetUserByIdView {
    private final DbConnectInit connection;

    public GetUserByIdView(DbConnectInit connection) {
        this.connection = connection;
    }

    public String getUserByIdProcessing(String idData) {
        String isValid;
        ReadParams readParamsById = new ReadParams();
        ValidateAnswer validateId = readParamsById.setId(idData);
        isValid = validateProcessing(validateId);
        if (!isValid.isEmpty()) {
            return isValid;
        }
        ReadController readControllerById = new ReadController();
        return getProcessedData(connection, readParamsById, readControllerById, true);
    }

}
