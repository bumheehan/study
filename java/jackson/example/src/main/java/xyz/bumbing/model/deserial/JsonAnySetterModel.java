package xyz.bumbing.model.deserial;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.ToString;

@ToString
public class JsonAnySetterModel {

    private String normalField;

    private Map<String, String> testMap = new HashMap<>();

    public String getNormalField() {
	return normalField;
    }

    public void setNormalField(String normalField) {
	this.normalField = normalField;
    }

    public Map<String, String> getTestMap() {
	return testMap;
    }

    @JsonAnySetter
    public void add(String key, String value) {
	testMap.put(key, value);
    }

}
