package com.ebay.tdq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;

/**
 * @author juntzhang
 */
public class JsonUtils {
  public static <T> T parseObject(String json, Class<T> c) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, c);
  }

  public static <T> T[] parseArray(String json, Class<T> c) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    TypeFactory typeFactory = mapper.getTypeFactory();
    return mapper.readValue(json, typeFactory.constructArrayType(c));
  }

  public static <T> String toJSONString(T o) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(o);
  }
}
