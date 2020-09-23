package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

@Slf4j
public abstract class BaseMetricsTest {

  protected JsonNode yaml;
  private ObjectMapper objectMapper =
      new ObjectMapper(new YAMLFactory())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  protected JsonNode loadTestCasesYaml(String fileName) throws Exception {
    String fullPathName = "src/test/resources/test-cases/metrics/" + fileName;
    File resourcesFile = new File(fullPathName);
    if (!resourcesFile.exists()) {
      throw new IOException("Cannot load test case file: " + fullPathName);
    }
    return objectMapper.readTree(resourcesFile);
  }

  protected List<DynamicTest> generateDynamicTests(
      JsonNode yaml, FieldMetrics<UbiEvent, SessionAccumulator> fieldMetrics) throws IOException {
    List<MetricsTestCase> testCases =
        objectMapper.readValue(yaml.toString(), new TypeReference<List<MetricsTestCase>>() {
        });

    List<DynamicTest> dynamicTestList = Lists.newArrayList();

    for (int i = 0; i < testCases.size(); i++) {
      final int caseNum = i;
      DynamicTest dynamicTest =
          DynamicTest.dynamicTest(
              testCases.get(caseNum).getName(),
              () -> {
                MetricsTestCase testCase = testCases.get(caseNum);
                UbiEvent ubiEvent = testCase.getInputs().getUbiEvent();
                SessionAccumulator sessionAccumulator =
                    testCase.getInputs().getSessionAccumulator();
                fieldMetrics.init();
                fieldMetrics.feed(ubiEvent, sessionAccumulator);
                UbiSession actual = sessionAccumulator.getUbiSession();
                Iterator<String> fieldNames =
                    yaml.get(caseNum).get("expect").get("UbiSession").fieldNames();

                // assert all fields
                while (fieldNames.hasNext()) {
                  String field = fieldNames.next();
                  JsonNode node = yaml.get(caseNum).get("expect").get("UbiSession").get(field);
                  Object fieldVal = FieldUtils.getField(actual.getClass(), field, true).get(actual);

                  if (Arrays.isArray(fieldVal)) {
                    Object[] results = (Object[]) fieldVal;
                    Object[] expectValues = StreamSupport.stream(node.spliterator(), false)
                                                         .map(JsonNode::asText)
                                                         .toArray();
                    Assertions.assertEquals(expectValues.length, results.length);
                    for (int j = 0; j < results.length; j++) {
                      Assertions.assertEquals(expectValues[j].toString(), String.valueOf(results[j]));
                    }
                  } else if (fieldVal instanceof Set) {
                    Set<Object> actualSet = (Set<Object>) fieldVal;

                    Set<String> stringSet = actualSet.stream()
                                                     .map(String::valueOf)
                                                     .collect(Collectors.toSet());

                    Set<String> nodes = StreamSupport.stream(node.spliterator(), false)
                                                     .map(JsonNode::asText)
                                                     .collect(Collectors.toSet());

                    Assertions.assertIterableEquals(stringSet, nodes);
                  } else {
                    if (node.isNull()) {
                      Assertions.assertNull(fieldVal);
                    } else {
                      Assertions.assertEquals(node.asText(), fieldVal.toString());
                    }
                  }
                }
              });
      dynamicTestList.add(dynamicTest);
    }

    return dynamicTestList;
  }
}
