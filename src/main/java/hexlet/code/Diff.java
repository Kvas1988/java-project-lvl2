package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
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

    // checktyle [HiddenField] Sucks Big Time!!!
    Diff(String fieldParam, JsonNode initValueParam, JsonNode modifiedValueParam, DiffStatus statusParam) {
        this.field = fieldParam;
        this.initValue = initValueParam;
        this.modifiedValue = modifiedValueParam;
        this.status = statusParam;
    }

    Diff(String fieldParam, JsonNode valueParam, DiffStatus statusParam) {
        this.field = fieldParam;
        this.status = statusParam;

        switch (statusParam) {
            case ADDED:
                this.initValue = null;
                this.modifiedValue = valueParam;
                break;
            case REMOVED:
                this.modifiedValue = null;
                this.initValue = valueParam;
                break;
            default:
                // EQUAL
                this.initValue = valueParam;
                this.modifiedValue = valueParam;

        }
    }



}
