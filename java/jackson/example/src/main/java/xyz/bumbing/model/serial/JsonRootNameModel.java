package xyz.bumbing.model.serial;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Builder;

@Builder
@JsonRootName("root")
public class JsonRootNameModel {

    private String normalField;

    private String testField;

    public void setNormalField(String normalField) {
	this.normalField = normalField;
    }

    public void setTestField(String testField) {
	this.testField = testField;
    }

    public String getNormalField() {
	return normalField;
    }

    public String getTestField() {
	return testField;
    }

    public static JsonRootNameModel getInstance() {

	return builder().normalField("normalField").testField("testField").build();
    }

}
