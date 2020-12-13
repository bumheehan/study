package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.ToString;

@ToString
@JsonAutoDetect(fieldVisibility = Visibility.PUBLIC_ONLY, setterVisibility = Visibility.PUBLIC_ONLY)
public class JsonAutoDetectModel {

    public String normalField;
    private String testField;

    public void setTestField(String testField) {
	this.testField = testField;
    }

}
