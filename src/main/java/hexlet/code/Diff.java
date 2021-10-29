package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;

public final class Diff {
    public enum DiffStatus {
        EQUAL,
        ADDED,
        REMOVED,
        MODIFIED
    }

    private final String field;
    private final JsonNode initValue;
    private final JsonNode modifiedValue;
    private final DiffStatus status;

    public String getField() {
        return field;
    }

    public JsonNode getInitValue() {
        return initValue;
    }

    public JsonNode getModifiedValue() {
        return modifiedValue;
    }

    public DiffStatus getStatus() {
        return status;
    }

    Diff(String field, JsonNode initValue, JsonNode modifiedValue, DiffStatus status) {
        this.field = field;
        this.initValue = initValue;
        this.modifiedValue = modifiedValue;
        this.status = status;
    }

    Diff(String field, JsonNode value, DiffStatus status) {
        this.field = field;
        this.status = status;

        switch (status) {
            case ADDED:
                this.initValue = null;
                this.modifiedValue = value;
                break;
            case REMOVED:
                this.modifiedValue = null;
                this.initValue = value;
                break;
            default:
                // EQUAL
                this.initValue = value;
                this.modifiedValue = value;

        }
    }



}
