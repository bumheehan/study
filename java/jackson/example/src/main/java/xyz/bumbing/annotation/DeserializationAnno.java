package xyz.bumbing.annotation;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.bumbing.model.deserial.DeserialBasicModel;
import xyz.bumbing.model.deserial.JsonAliasModel;
import xyz.bumbing.model.deserial.JsonAnySetterModel;
import xyz.bumbing.model.deserial.JsonCreatorModel;
import xyz.bumbing.model.deserial.JsonDeserializeModel;
import xyz.bumbing.model.deserial.JsonInjectModel;
import xyz.bumbing.model.deserial.JsonSetterModel;

public class DeserializationAnno {
    static ObjectMapper om = new ObjectMapper();

    static <T> void deserialize(Class<T> clazz, String raw) {
	String json = "{\"normalField\":\"normalField\",\"testField\":\"testField\",\"normalMap\":{\"normalMapKey\":\"normalMapValue\"},\"testMap\":{\"testMapKey\":\"testMapValue\"}}";
	if (raw != null) {
	    json = raw;
	}
	try {
	    println(om.readValue(json, clazz));
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

    }

    static void println(Object object) {
	System.out.println(object);
    }

    public static void main(String[] args) {
	println("Json normal 예제");
	deserialize(DeserialBasicModel.class, null);
	println("------------------------------------");

	println("JsonCreator 예제");
	deserialize(JsonCreatorModel.class, null);
	println("------------------------------------");

	println("JsonInject 예제");
	InjectableValues.Std injectableValues = new InjectableValues.Std();
	injectableValues.addValue("testField", "JsonInject");
	Map<String, String> map = new HashMap<>();
	map.put("testMAP", "JsonInject");
	injectableValues.addValue("testMap", map);
	om.setInjectableValues(injectableValues);
	deserialize(JsonInjectModel.class, null);
	println("------------------------------------");

	println("JsonAnySetter 예제");
	String json = "{\"normalField\":\"normalField\",\"testField\":\"testField\"}";
	deserialize(JsonAnySetterModel.class, json);
	println("------------------------------------");

	println("JsonSetter 예제");
	deserialize(JsonSetterModel.class, json);
	println("------------------------------------");

	println("JsonAlias 예제");
	json = "{\"normalField\":\"normalField\",\"testField\":\"testField\",\"test1\":\"testField1\"}";
	deserialize(JsonAliasModel.class, json);
	println("------------------------------------");

	println("JsonDeserialize 예제");
	json = "{\"testField\":\"13-12-2020 06:49:02\"}";
	deserialize(JsonDeserializeModel.class, json);
	println("------------------------------------");

    }
}
