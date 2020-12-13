package xyz.bumbing.model.serial;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;

@Builder
public class JsonValueModel {

    private String normalField;

    private String testField;

    public void setNormalField(String normalField) {
	this.normalField = normalField;
    }

    public void setTestField(String testField) {
	this.testField = testField;
    }

    @JsonValue
    public String printJson() {
	return "JsonValue Annotation";

    }

    public static JsonValueModel getInstance() {

	return builder().normalField("normalField").testField("testField").build();
    }

}
