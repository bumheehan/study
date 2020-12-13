package xyz.bumbing.annotation;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.bumbing.custom.Views;
import xyz.bumbing.model.general.JsonFormatModel;
import xyz.bumbing.model.general.JsonPropertyModel;
import xyz.bumbing.model.general.JsonUnwrappedModel;
import xyz.bumbing.model.general.JsonViewModel;

public class JacksonGeneralAnno {
    static ObjectMapper om = new ObjectMapper();

    static <T> T deserialize(Class<T> clazz, String json) {
	T retVal = null;
	try {
	    retVal = om.readValue(json, clazz);
	    println(retVal);
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}
	return retVal;
    }

    static void serialize(Object object) {
	try {
	    System.out.println(om.writeValueAsString(object));
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}
    }

    static void println(Object object) {
	System.out.println(object);
    }

    public static void main(String[] args) {
	String json = "{\"normalField\":\"normalField\",\"testField\":\"testField\"}";

	println("JsonProperty 예제");
	deserialize(JsonPropertyModel.class, json);
	serialize(JsonPropertyModel.builder().normalField("normalField").testField("testField").build());
	println("------------------------------------");

	println("JsonFormat 예제");
	json = "{\"testField\":\"14-12-2020 12:13:14\"}";
	deserialize(JsonFormatModel.class, json);
	serialize(JsonFormatModel.builder().testField(new Date()).build());
	println("------------------------------------");

	println("JsonUnwrapped 예제");
	//	json = "{\"normalField\":\"normalField\", \"testVV\" : {\"t1\":\"t1\",\"t2\":\"t2\"} }";
	json = "{\"normalField\":\"normalField\", \"t1\":\"t1\",\"t2\":\"t2\"}";
	deserialize(JsonUnwrappedModel.class, json);
	serialize(JsonUnwrappedModel.builder().testVV(JsonUnwrappedModel.getVV()).normalField("normalField").build());
	println("------------------------------------");

	println("JsonView 예제");
	json = "{\"id\":1,\"itemName\":\"item\",\"ownerName\":\"owner\"}";
	try {
	    //deserialize
	    JsonViewModel read = om.readerWithView(Views.Public.class).readValue(json, JsonViewModel.class);
	    println(read);

	    //serialize
	    String write = om.writerWithView(Views.Public.class)
		    .writeValueAsString(new JsonViewModel(1, "item", "owner"));
	    println(write);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	println("------------------------------------");
    }
}
