package org.example.app.app.utils;
import org.example.app.app.utils.validate.Validator;
import org.example.app.app.utils.validate.enums.EValidateQuery;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.ArrayList;
import java.util.List;

public class ReadParams {
    private final int limit;
    private final int offset;
    private final List<String> excludeColumns;
    private final Validator<EValidateQuery> validator = new Validator<>();
    private Long id;


    public ValidateAnswer setId(String id) {
        ValidateAnswer answer = validator.validate(id, EValidateQuery.ID);
        if (answer.isValid()) {
            this.id = Long.parseLong(id);
            return answer;
        }
        return answer;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public List<String> getExcludeColumns() {
        return excludeColumns;
    }
    public Long getId() {
        return id;
    }



    public ReadParams() {
        this.limit = 0;
        this.offset = 0;
        this.excludeColumns = new ArrayList<>();
    }

}
