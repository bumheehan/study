package xyz.bumbing.model.deserial;

import java.util.Map;

import com.fasterxml.jackson.annotation.JacksonInject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JsonInjectModel {

    private String normalField;

    @JacksonInject("testField")
    private String testField;

    private Map<String, String> normalMap;

    @JacksonInject("testMap")
    private Map<String, String> testMap;

}
