package org.example.app.app.model;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.repository.UserRepository;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.CreateUpdateParams;


public class CreateUserModel {

    public CreateUserModel() {
    }

    public ActionAnswer createUser (DbConnectInit connection, CreateUpdateParams createParams) {
        User newUser = new User(createParams.getFirstName(), createParams.getLastName(), createParams.getEmail(), createParams.getPhone());
        UserRepository userRepository = new UserRepository(connection);
        return userRepository.create(newUser);
    }

}
