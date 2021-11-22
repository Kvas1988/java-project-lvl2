package hexlet.code;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = DiffSerializer.class)
public final class Diff {
    public enum DiffStatus {
        EQUAL,
        ADDED,
        REMOVED,
        MODIFIED
    }

    private final String field;
    private final Object initValue;
    private final Object modifiedValue;
    private final DiffStatus status;

    public String getField() {
        return field;
    }

    public Object getInitValue() {
        return initValue;
    }

    public Object getModifiedValue() {
        return modifiedValue;
    }

    public DiffStatus getStatus() {
        return status;
    }

    // checktyle [HiddenField] Sucks Big Time!!!
    Diff(String fieldParam, Object initValueParam, Object modifiedValueParam, DiffStatus statusParam) {
        this.field = fieldParam;
        this.initValue = initValueParam;
        this.modifiedValue = modifiedValueParam;
        this.status = statusParam;
    }
}
