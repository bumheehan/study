package xyz.bumbing.model.serial;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;
import lombok.Getter;
import xyz.bumbing.custom.CustomZondDateTimeSerializer;

@Builder
@Getter
public class JsonSerializeModel {

    private ZonedDateTime normalField;

    @JsonSerialize(using = CustomZondDateTimeSerializer.class)
    private ZonedDateTime testField;

    public static JsonSerializeModel getInstance() {

	return builder().normalField(ZonedDateTime.now()).testField(ZonedDateTime.now()).build();
    }

}
