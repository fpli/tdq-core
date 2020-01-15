package com.ebay.sojourner.ubd.common.attributes;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class AgentAttributeTest extends BaseAttributesTest<AgentIpAttribute>{
    private AgentAttribute agentAttribute;
    private Pair<JsonNode, List<AttributesTestCase>> pair;

    @BeforeEach
    public void setup() throws Exception {
        agentAttribute = new AgentAttribute();
        pair = loadTestCases("agent-attribute.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(pair.getRight(), pair.getLeft(), agentAttribute);
    }
}
