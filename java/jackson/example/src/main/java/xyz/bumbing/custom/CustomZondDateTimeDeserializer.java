package xyz.bumbing.custom;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomZondDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public CustomZondDateTimeDeserializer() {
	this(null);
    }

    public CustomZondDateTimeDeserializer(Class<ZonedDateTime> t) {
	super(t);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt)
	    throws IOException, JsonProcessingException {
	LocalDateTime local = LocalDateTime.parse(p.getText(), formatter);
	return local.atZone(ZoneId.of("Asia/Seoul"));
    }

}
