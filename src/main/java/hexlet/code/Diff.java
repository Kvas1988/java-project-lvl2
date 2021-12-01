package hexlet.code;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Diff {
    public enum DiffStatus {
        EQUAL,
        ADDED,
        REMOVED,
        MODIFIED
    }

    @JsonProperty("file1")
    private final Object value1;
    @JsonProperty("file2")
    private final Object value2;
    @JsonIgnore
    private final DiffStatus status;


    public Object getValue1() {
        return value1;
    }

    public Object getValue2() {
        return value2;
    }

    public DiffStatus getStatus() {
        return status;
    }

    // checktyle [HiddenField] Sucks Big Time!!!
    Diff(Object value1Param, Object value2Param, DiffStatus statusParam) {
        this.value1 = value1Param;
        this.value2 = value2Param;
        this.status = statusParam;
    }
}
