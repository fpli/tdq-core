package com.ebay.sojourner.common.attributes;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.SessionCore;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class AgentIpAttributeTest extends BaseAttributesTest<SessionCore> {

  private AgentIpAttribute agentIpAttribute;
  private Pair<JsonNode, List<AttributesTestCase>> pair;

  @BeforeEach
  public void setup() throws Exception {
    agentIpAttribute = new AgentIpAttribute();
    pair = loadTestCases("agent-ip-attribute.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(pair.getRight(), pair.getLeft(), agentIpAttribute);
  }
}
