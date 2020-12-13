package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JsonIgnoreModel {

    private String normalField;
    @JsonIgnore
    private String testField;

}
