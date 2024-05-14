package org.example.app.app.utils;

import org.example.app.app.entity.User;
import org.example.app.app.utils.validate.Validator;
import org.example.app.app.utils.validate.enums.EValidateQuery;
import org.example.app.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.ArrayList;
import java.util.List;

public class ReadParams {
    private int limit;
    private int offset;
    private List<String> excludeColumns;
    private final Validator<EValidateQuery> validator = new Validator<>();
    private Long id;

    public ValidateAnswer setLimit(String limit) {
        ValidateAnswer answer = validator.validate(limit, EValidateQuery.LIMIT);
        if (answer.isValid()) {
            this.limit = Integer.parseInt(limit);
            return answer;
        }
        return answer;
    }

    public ValidateAnswer setOffset(String offset) {
        ValidateAnswer answer = validator.validate(offset, EValidateQuery.OFFSET);
        if (answer.isValid()) {
            this.offset = Integer.parseInt(offset);
            return answer;
        }
        return answer;
    }

    public ValidateAnswer setExcludeColumns(String excludeColumns) {
        ValidateAnswer answer = validator.validate(excludeColumns, EValidateQuery.EXCLUDE_COLUMNS);
        List<String> columnsNotInTable = new ArrayList<>();
        if (answer.isValid()) {
            List<String> columnsFromUser= List.of(excludeColumns.split(","));
            AnnotationUtils annotationUtils = new AnnotationUtils();
            List<String> columnsInTable = annotationUtils.getColumnNames(User.class);
            columnsFromUser.forEach(column -> {
                if (!columnsInTable.contains(column)) {
                    columnsNotInTable.add(column);
                }
            });
            if (!columnsNotInTable.isEmpty()) {
                ValidateAnswer errorAnswer = new ValidateAnswer();
                columnsNotInTable.forEach(column -> errorAnswer.addError("Column " + column + " not in table"));
                return errorAnswer;
            }
            this.excludeColumns = columnsFromUser;
            return answer;
        }
        return answer;
    }

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
