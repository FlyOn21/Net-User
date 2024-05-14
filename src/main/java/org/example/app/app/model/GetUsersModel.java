package org.example.app.app.model;

import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.entity.User;
import org.example.app.app.repository.UserRepository;
import org.example.app.app.utils.ReadParams;

import java.util.List;
import java.util.Optional;

public class GetUsersModel {
    private final int limit;
    private final int offset;
    private final List<String> excludeColumns;
    private final Long id;

    public GetUsersModel(ReadParams readParams) {
        this.limit = readParams.getLimit();
        this.offset = readParams.getOffset();
        this.excludeColumns = readParams.getExcludeColumns();
        this.id = readParams.getId();
    }


    public Optional<List<User>> getUsers(DbConnectInit connection) {
        UserRepository userRepository = new UserRepository(connection);
        return userRepository.readAll(this.excludeColumns, this.limit, this.offset);
    }

    public Optional<List<User>> getUser(DbConnectInit connection) {
        UserRepository userRepository = new UserRepository(connection);
        return userRepository.readById(this.id, this.excludeColumns);
    }

}
