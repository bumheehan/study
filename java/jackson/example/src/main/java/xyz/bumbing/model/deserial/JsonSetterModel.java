package xyz.bumbing.model.deserial;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.ToString;

@ToString
public class JsonSetterModel {

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

    public void setTestField(String testField) {
	this.testField = testField;
    }

    @JsonSetter(value = "testField")
    public void testSetter(String testField) {
	this.testField = "TestSetter " + testField;
    }
}
