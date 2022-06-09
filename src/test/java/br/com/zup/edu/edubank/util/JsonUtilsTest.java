package br.com.zup.edu.edubank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtilsTest {
    @Autowired
    private ObjectMapper mapper;

    public <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public <T> T toObject(Class<T> type, String json) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }


    public <T> List<T> toListObject(Class<T> type, String json) throws JsonProcessingException {

        TypeFactory typeFactory = mapper.getTypeFactory();

        return mapper.readValue(
                json,
                typeFactory.constructCollectionType(
                        List.class,
                        type
                )
        );
    }

    public <T,S> Map<T,S> toMapObject(Class<T> key, Class<S> value, String json) throws JsonProcessingException {

        TypeFactory typeFactory = mapper.getTypeFactory();

        return mapper.readValue(
                json,
                typeFactory.constructMapType(HashMap.class,key,value)
        );
    }

    public <T> List<T> toListObjectResultPage(Class<T> type, String json) throws JsonProcessingException {

        JsonPath jsonPath = JsonPath.compile("@.content");
        Object content = jsonPath.read(json);

        TypeFactory typeFactory = mapper.getTypeFactory();

        return mapper.convertValue(
                content,
                typeFactory.constructCollectionType(
                        List.class,
                        type
                )
        );
    }
}
