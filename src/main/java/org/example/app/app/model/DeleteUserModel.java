package org.example.app.app.model;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.repository.UserRepository;
import org.example.app.app.utils.ActionAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteUserModel {
    private final Long id;

    public DeleteUserModel(User user) {
        this.id = user.getId();
    }

    public ActionAnswer deleteUser(DbConnectInit connection) {
        UserRepository userRepository = new UserRepository(connection);
        Optional<List<User>> answerGetById = userRepository.readById(this.id, new ArrayList<>());
        if (answerGetById.isPresent()) {
            List<User> users = answerGetById.get();
            if (users.size() == 1) {
                return userRepository.delete(this.id);
            }
        }
        return new ActionAnswer(false, "User not found");
    }
}
