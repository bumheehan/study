package xyz.bumbing.model.deserial;

import java.util.HashMap;
import java.util.Map;

import lombok.ToString;

@ToString
public class DeserialBasicModel {

    private String normalField;

    private String testField;

    private Map<String, String> normalMap = new HashMap<>();

    private Map<String, String> testMap = new HashMap<>();

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

    public Map<String, String> getTestMap() {
	return testMap;
    }

    public void setTestMap(Map<String, String> testMap) {
	this.testMap = testMap;
    }

}
