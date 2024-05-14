package org.example.app.app.views;

import org.example.app.app.controllers.ReadController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.utils.ReadParams;

import static org.example.app.app.utils.GetDataProcessing.getProcessedData;

public class GetUsersView {
    private final DbConnectInit connection;

    public GetUsersView(DbConnectInit connection) {
        this.connection = connection;
    }

    public String getUsersViewProcessing() {
        ReadParams readParams = new ReadParams();
        ReadController readControllerWithout = new ReadController();
        return getProcessedData(connection, readParams, readControllerWithout, false);
    }


}
