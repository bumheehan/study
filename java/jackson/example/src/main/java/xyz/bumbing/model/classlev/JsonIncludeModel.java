package xyz.bumbing.model.classlev;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(value = Include.NON_EMPTY)
public class JsonIncludeModel {

    private String normalField;
    private String testField;

}
