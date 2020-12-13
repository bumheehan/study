package xyz.bumbing.model.deserial;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class JsonCreatorModel {

    private String normalField;

    private String testField;

    private Map<String, String> normalMap;

    private Map<String, String> testMap;

    @JsonCreator
    public JsonCreatorModel(@JsonProperty("normalField") String nf, @JsonProperty("testField") String tf,
	    @JsonProperty("normalMap") Map<String, String> nm, @JsonProperty("testMap") Map<String, String> testMap) {

	tf = "JsonCreator 메소드";
	this.normalField = nf;
	this.testField = tf;
	this.normalMap = nm;
	this.testMap = testMap;
    }

}
