package com.ebay.sojourner.common.attributes;

import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.SessionCore;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class GuidAttributeTest extends BaseAttributesTest<SessionCore> {

  private GuidAttribute guidAttribute;
  private Pair<JsonNode, List<AttributesTestCase>> pair;

  @BeforeEach
  public void setup() throws Exception {
    guidAttribute = new GuidAttribute();
    pair = loadTestCases("guid-attribute.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(pair.getRight(), pair.getLeft(), guidAttribute);
  }
}
