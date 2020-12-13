package xyz.bumbing.model.serial;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;

@Builder
@JsonPropertyOrder(value = { "testField", "normalField" })
public class JsonPropertyOrderModel {

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

    public static JsonPropertyOrderModel getInstance() {

	return builder().normalField("normalField").testField("testField").build();
    }

}
