package xyz.bumbing.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import xyz.bumbing.model.serial.JsonAnyGetterModel;
import xyz.bumbing.model.serial.JsonGetterModel;
import xyz.bumbing.model.serial.JsonPropertyOrderModel;
import xyz.bumbing.model.serial.JsonRawValueModel;
import xyz.bumbing.model.serial.JsonRootNameModel;
import xyz.bumbing.model.serial.JsonSerializeModel;
import xyz.bumbing.model.serial.JsonValueModel;

public class SerializationAnno {
    static ObjectMapper om = new ObjectMapper();

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
	//JsonAnyGetter 
	println("JsonAnyGetter 예제");
	serialize(JsonAnyGetterModel.getInstance());
	println("------------------------------------");

	//JsonGetter
	println("JsonGetter");
	serialize(JsonGetterModel.getInstance());
	println("------------------------------------");

	//JsonPropertyOrder
	println("JsonPropertyOrder");
	serialize(JsonPropertyOrderModel.getInstance());
	println("------------------------------------");

	//JsonRawValue
	println("JsonRawValue");
	serialize(JsonRawValueModel.getInstance());
	println("------------------------------------");

	//JsonValue
	println("JsonValue");
	serialize(JsonValueModel.getInstance());
	println("------------------------------------");

	//JsonValue
	println("JsonRootName");
	om.enable(SerializationFeature.WRAP_ROOT_VALUE);
	serialize(JsonRootNameModel.getInstance());
	om.disable(SerializationFeature.WRAP_ROOT_VALUE);
	println("------------------------------------");

	//JsonSerialize
	println("JsonSerialize");
	serialize(JsonSerializeModel.getInstance());
	println("------------------------------------");
    }
}
