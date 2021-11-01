package hexlet.code;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public final class DiffSerializer extends StdSerializer<Diff> {

    public DiffSerializer() {
        this(null);
    }

    public DiffSerializer(Class<Diff> t) {
        super(t);
    }


    @Override
    public void serialize(Diff value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // gen.writeStartObject();
        // gen.writeFieldName(value.getField());
        // gen.writeStartArray();
        //
        // switch (value.getStatus()) {
        //     case ADDED:
        //         gen.writeString("  + " + value.getModifiedValue().toString());
        //         break;
        //     case REMOVED:
        //         gen.writeString("  - " + value.getInitValue().toString());
        //         break;
        //     case MODIFIED:
        //         gen.writeString("  - " + value.getInitValue().toString());
        //         gen.writeString("  + " + value.getModifiedValue().toString());
        //         break;
        //     case EQUAL:
        //         gen.writeString("    " + value.getModifiedValue().toString());
        //         break;
        //     default:
        //         break;
        // }
        //
        // gen.writeEndArray();
        // gen.writeEndObject();

        gen.writeStartObject();
        gen.writeFieldName(value.getField());
        gen.writeStartObject();

        gen.writeObjectField("file1", value.getInitValue());
        gen.writeObjectField("file2", value.getInitValue());

        gen.writeEndObject();
        gen.writeEndObject();


    }
}
