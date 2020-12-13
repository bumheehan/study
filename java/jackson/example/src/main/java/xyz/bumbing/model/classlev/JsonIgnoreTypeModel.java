package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JsonIgnoreTypeModel {

    private String normalField;

    private TestType testType;

    public void create() {
	TestType testType = new TestType();
	testType.setTest1("test1");
	testType.setTest2("test2");
	this.testType = testType;
    }
}

@Getter
@Setter
@ToString
@JsonIgnoreType
class TestType {
    private String test1;
    private String test2;
}
