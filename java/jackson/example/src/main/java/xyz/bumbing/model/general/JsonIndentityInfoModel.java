package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class JsonIndentityInfoModel {

    public int id;
    public String itemName;
    public JsonIndentityInfoModel2 back;

    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public static class JsonIndentityInfoModel2 {

	public int id;
	public String name;
	public JsonIndentityInfoModel managed;

    }
}
