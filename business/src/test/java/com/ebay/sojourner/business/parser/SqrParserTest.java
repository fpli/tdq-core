package com.ebay.sojourner.business.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SqrParserTest extends BaseParsersTest {

    private SqrParser sqrParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    sqrParser = new SqrParser();
    yaml = loadTestCasesYaml("SqrParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, sqrParser);
  }
}
