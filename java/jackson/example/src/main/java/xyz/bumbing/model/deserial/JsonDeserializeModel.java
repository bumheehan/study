package xyz.bumbing.model.deserial;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Setter;
import lombok.ToString;
import xyz.bumbing.custom.CustomZondDateTimeDeserializer;

@ToString
@Setter
public class JsonDeserializeModel {

    @JsonDeserialize(using = CustomZondDateTimeDeserializer.class)
    private ZonedDateTime testField;

}
