package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonPropertyModel {

    private String normalField;

    private String testField;

    public String getNormalField() {
	return normalField;
    }

    public void setNormalField(String normalField) {
	this.normalField = normalField;
    }

    @JsonProperty("testField")
    public String customGetterTestField() {
	System.out.println("customGetter");
	return testField;
    }

    @JsonProperty("testField")
    public void customSetterTestField(String testField) {
	System.out.println("customSetter");
	this.testField = testField;
    }

}
