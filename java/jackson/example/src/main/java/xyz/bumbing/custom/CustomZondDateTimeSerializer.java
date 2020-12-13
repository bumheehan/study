package xyz.bumbing.custom;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomZondDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public CustomZondDateTimeSerializer() {
	this(null);
    }

    public CustomZondDateTimeSerializer(Class<ZonedDateTime> t) {
	super(t);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
	String date = value.format(formatter);
	gen.writeString(date);
    }

}
