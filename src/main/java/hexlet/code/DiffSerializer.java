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

        gen.writeStartObject();
        gen.writeFieldName(value.getField());
        gen.writeStartObject();

        gen.writeObjectField("file1", value.getInitValue());
        gen.writeObjectField("file2", value.getModifiedValue());

        gen.writeEndObject();
        gen.writeEndObject();
    }
}
