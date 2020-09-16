package com.ebay.sojourner.business.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class CiidParserTest extends BaseParsersTest {

  private CiidParser ciidParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    ciidParser = new CiidParser();
    yaml = loadTestCasesYaml("CiidParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, ciidParser);
  }
}
