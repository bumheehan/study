package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(value = { "testField" })
public class JsonIgnorePropertiesModel {

    private String normalField;

    private String testField;

}
