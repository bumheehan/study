package xyz.bumbing.model.serial;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import lombok.Builder;

@Builder
public class BasicModel {

    private String normalField;

    private String testField;

    private Map<String, String> normalMap;

    private Map<String, String> testMap;

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

    public Map<String, String> getNormalMap() {
	return normalMap;
    }

    public void setNormalMap(Map<String, String> normalMap) {
	this.normalMap = normalMap;
    }

    @JsonAnyGetter(enabled = true)
    public Map<String, String> getTestMap() {
	return testMap;
    }

    public void setTestMap(Map<String, String> testMap) {
	this.testMap = testMap;
    }

}
