package xyz.bumbing.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.bumbing.model.classlev.JsonAutoDetectModel;
import xyz.bumbing.model.classlev.JsonIgnoreModel;
import xyz.bumbing.model.classlev.JsonIgnorePropertiesModel;
import xyz.bumbing.model.classlev.JsonIgnorePropertiesModel2;
import xyz.bumbing.model.classlev.JsonIgnoreTypeModel;
import xyz.bumbing.model.classlev.JsonIncludeModel;

public class JsonClassLevelAnno {
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
	println("JsonIgnoreProperties 예제1");
	deserialize(JsonIgnorePropertiesModel.class, json);
	JsonIgnorePropertiesModel jsonIgnorePropertiesModel = new JsonIgnorePropertiesModel();
	jsonIgnorePropertiesModel.setTestField("testField");
	jsonIgnorePropertiesModel.setNormalField("normalField");
	serialize(jsonIgnorePropertiesModel);
	println("------------------------------------");

	json = "{\"normalField\":\"normalField\",\"testField\":\"testField\",\"testField1\":\"testField1\"}";
	println("JsonIgnoreProperties 예제2");
	deserialize(JsonIgnorePropertiesModel2.class, json);
	serialize(jsonIgnorePropertiesModel);
	println("------------------------------------");

	json = "{\"normalField\":\"normalField\",\"testField\":\"testField\"}";
	println("JsonIgnore 예제");
	deserialize(JsonIgnoreModel.class, json);
	JsonIgnoreModel jsonIgnoreModel = new JsonIgnoreModel();
	jsonIgnoreModel.setNormalField("normalField");
	jsonIgnoreModel.setTestField("testField");
	serialize(jsonIgnoreModel);
	println("------------------------------------");

	json = "{\"normalField\":\"normalField\",\"testType\":{\"test1\":\"test1\",\"test2\":\"test2\"}}";
	println("JsonIgnoreType 예제");
	deserialize(JsonIgnoreTypeModel.class, json);
	JsonIgnoreTypeModel jsonIgnoreTypeModel = new JsonIgnoreTypeModel();
	jsonIgnoreTypeModel.setNormalField("normalField");
	jsonIgnoreTypeModel.create();
	serialize(jsonIgnoreTypeModel);
	println("------------------------------------");

	json = "{\"normalField\":\"normalField\",\"testField\":\"testField\"}";
	println("JsonInclude 예제");
	deserialize(JsonIncludeModel.class, json);
	JsonIncludeModel jsonIncludeModel = new JsonIncludeModel();
	jsonIncludeModel.setNormalField("normalField");
	jsonIncludeModel.setTestField("");
	serialize(jsonIncludeModel);
	println("------------------------------------");

	println("JsonAutoDetect 예제");
	deserialize(JsonAutoDetectModel.class, json);
	println("------------------------------------");

    }
}
