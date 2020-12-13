package xyz.bumbing.model.deserial;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JsonAliasModel {

    private String normalField;

    @JsonAlias(value = { "test_1", "test1" })
    private String testField;

}
