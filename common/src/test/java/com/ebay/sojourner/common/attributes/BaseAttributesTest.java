package com.ebay.sojourner.common.attributes;

import com.ebay.sojourner.common.model.Attribute;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

public class BaseAttributesTest<T> {

  private AttributeTestInputObjects attributesInput;
  private AttributesTestCase attributesTestCase;
  private List<AttributesTestCase> attributesTestCaseList;

  private ObjectMapper objectMapper =
      new ObjectMapper(new YAMLFactory())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  protected Pair<JsonNode, List<AttributesTestCase>> loadTestCases(String fileName)
      throws Exception {
    attributesTestCaseList = Lists.newArrayList();
    String fullPathName = "src/test/resources/test-cases/attributes/" + fileName;
    File resourcesFile = new File(fullPathName);
    JsonNode jsonNode = objectMapper.readTree(resourcesFile);
    for (int i = 0; i < jsonNode.size(); i++) {
      int caseNum = i;
      attributesTestCase = new AttributesTestCase();
      attributesInput = new AttributeTestInputObjects();
      JsonNode node = jsonNode.get(caseNum);
      JsonNode inputNode = node.get("input");
      String typeName = node.get("type").asText();
      Class<?> clazz = Class.forName("com.ebay.sojourner.ubd.common.model." + typeName);
      Object inputObjectValue = objectMapper.treeToValue(inputNode.get(typeName), clazz);
      Field inputObjectType =
          attributesInput
              .getClass()
              .getDeclaredField(typeName.substring(0, 1).toLowerCase() + typeName.substring(1));
      inputObjectType.setAccessible(true);
      inputObjectType.set(attributesInput, inputObjectValue);
      attributesInput.setBotFlag(inputNode.get("botFlag").asInt());
      attributesInput.setNeeded(inputNode.get("needed").asBoolean());

      attributesTestCase.setInput(attributesInput);
      attributesTestCase.setName(node.get("name").asText());
      attributesTestCase.setType(typeName);
      attributesTestCase.setExpectResult(node.get("expectResult").asText());
      attributesTestCaseList.add(attributesTestCase);
    }
    return Pair.of(jsonNode, attributesTestCaseList);
  }

  protected List<DynamicTest> generateDynamicTests(
      List<AttributesTestCase> testCases, JsonNode yaml, Attribute<T> attribute) {
    List<DynamicTest> dynamicTestList = Lists.newArrayList();

    for (int i = 0; i < testCases.size(); i++) {
      int caseNum = i;
      DynamicTest dynamicTest =
          DynamicTest.dynamicTest(
              testCases.get(i).getName(),
              () -> {
                AttributesTestCase attributesTestCase = testCases.get(caseNum);
                String typeName = attributesTestCase.getType();
                AttributeTestInputObjects attributesInput = attributesTestCase.getInput();
                Field inputObjectType =
                    attributesInput
                        .getClass()
                        .getDeclaredField(
                            typeName.substring(0, 1).toLowerCase() + typeName.substring(1));
                inputObjectType.setAccessible(true);
                T t = (T) inputObjectType.get(attributesInput);
                attribute.feed(
                    t,
                    attributesTestCase.getInput().getBotFlag());
                Iterator<String> fieldNames = yaml.get(caseNum).get("expectResult").fieldNames();
                fieldNames.forEachRemaining(
                    field -> {
                      JsonNode node = yaml.get(caseNum).get("expectResult").get(field);
                      try {
                        Object actualValue =
                            FieldUtils.getField(attribute.getClass(), field, true).get(attribute);
                        if (Arrays.isArray(actualValue)) {
                          Object[] array = (Object[]) actualValue;
                          for (int j = 0; j < array.length; j++) {
                            Assertions.assertEquals(node.get(j).asText(), String.valueOf(array[j]));
                          }
                        } else if (actualValue instanceof Set) {
                          Set actualValue1 = (Set) actualValue;
                          Set<String> actualSet = new HashSet<>();
                          Iterator<String> iterator = actualValue1.iterator();
                          while (iterator.hasNext()) {
                            actualSet.add(iterator.next());
                          }

                          Iterator<JsonNode> iterator1 = node.iterator();
                          while (iterator1.hasNext()) {
                            Assertions.assertEquals(
                                actualSet.contains(iterator1.next().asText()), true);
                          }

                          Assertions.assertEquals(node.size(), actualSet.size());
                        } else {
                          Assertions.assertEquals(node.asText(), actualValue.toString());
                        }
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    });
                attribute.clear();
              });

      dynamicTestList.add(dynamicTest);
    }
    return dynamicTestList;
  }
}
