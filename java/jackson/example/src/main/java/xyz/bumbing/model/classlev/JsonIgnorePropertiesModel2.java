package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonIgnorePropertiesModel2 {

    private String normalField;

    private String testField;

}
