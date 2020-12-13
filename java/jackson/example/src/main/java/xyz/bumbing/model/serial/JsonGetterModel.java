package xyz.bumbing.model.serial;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.Builder;

@Builder
public class JsonGetterModel {

    private String normalField;

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

    @JsonGetter("testField")
    public String getTestField2() {
	testField = "JsonGetter" + testField;
	return testField;
    }

    public void setTestField(String testField) {
	this.testField = testField;
    }

    public static JsonGetterModel getInstance() {

	return builder().normalField("normalField").testField("testField").build();
    }

}
