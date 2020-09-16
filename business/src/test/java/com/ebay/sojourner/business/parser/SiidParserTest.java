package com.ebay.sojourner.business.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SiidParserTest extends BaseParsersTest {

  private SiidParser siidParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    siidParser = new SiidParser();
    yaml = loadTestCasesYaml("SiidParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, siidParser);
  }
}
