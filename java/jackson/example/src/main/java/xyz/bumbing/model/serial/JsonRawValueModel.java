package xyz.bumbing.model.serial;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Builder;

@Builder
public class JsonRawValueModel {

    private String normalField;

    @JsonRawValue
    private String testField;

    public String getNormalField() {
	return normalField;
    }

    public void setNormalField(String normalField) {
	this.normalField = normalField;
    }

    public String getTestField() {
	return testField;
    }

    public void setTestField(String testField) {
	this.testField = testField;
    }

    public static JsonRawValueModel getInstance() {

	return builder().normalField("{\n  \"attr\":false\n}").testField("[\n  \"attr\":false\n}").build();
    }

}
