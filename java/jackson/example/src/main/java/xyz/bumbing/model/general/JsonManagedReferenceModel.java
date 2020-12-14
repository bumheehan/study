package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JsonManagedReferenceModel {

    public int id;
    public String itemName;
    @JsonManagedReference
    public JsonBackReferenceModel back;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class JsonBackReferenceModel {

	public int id;
	public String name;
	@JsonBackReference
	public JsonManagedReferenceModel managed;

    }
}
