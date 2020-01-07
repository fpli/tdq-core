package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.FieldMetrics;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public abstract class BaseMetricsTest {

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

    protected Pair<JsonNode, List<MetricsTestCase>> loadTestCases(String fileName) throws Exception {
        String fullPathName = "src/test/resources/test-cases/metrics/" + fileName;
        File resourcesFile = new File(fullPathName);
        JsonNode jsonNode = objectMapper.readTree(resourcesFile);
        List<MetricsTestCase> testCases = objectMapper.readValue(resourcesFile,
                new TypeReference<List<MetricsTestCase>>() {});

        return Pair.of(jsonNode, testCases);
    }

    protected List<DynamicTest> generateDynamicTests(List<MetricsTestCase> testCases, JsonNode yaml,
                                                     FieldMetrics<UbiEvent, SessionAccumulator> fieldMetrics) {
        List<DynamicTest> dynamicTestList = Lists.newArrayList();

        for (int i = 0; i < testCases.size(); i++) {
            int caseNum = i;
            DynamicTest dynamicTest = DynamicTest.dynamicTest(testCases.get(i).getName(), () -> {
                MetricsTestCase testCase = testCases.get(caseNum);
                UbiEvent ubiEvent = testCase.getInputs().getUbiEvent();
                SessionAccumulator sessionAccumulator = testCase.getInputs().getSessionAccumulator();
                fieldMetrics.init();
                fieldMetrics.feed(ubiEvent, sessionAccumulator);
                UbiSession actual = sessionAccumulator.getUbiSession();
                Iterator<String> fieldNames = yaml.get(caseNum).get("expect").get("UbiSession").fieldNames();
                fieldNames.forEachRemaining(field -> {
                    JsonNode node = yaml.get(caseNum).get("expect").get("UbiSession").get(field);
                    try {
                        Object actualValue = FieldUtils.getField(actual.getClass(), field, true).get(actual);
                        if (Arrays.isArray(actualValue)) {
                            Object[] array = (Object[]) actualValue;
                            for (int j = 0; j < array.length; j++) {
                                Assertions.assertEquals(node.get(j).asText(), String.valueOf(array[j]));
                            }
                        } else {
                            Assertions.assertEquals(node.asText(), actualValue.toString());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });

            dynamicTestList.add(dynamicTest);
        }

        return dynamicTestList;
    }

}
