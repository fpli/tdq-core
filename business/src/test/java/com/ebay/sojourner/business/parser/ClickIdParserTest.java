package com.ebay.sojourner.business.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@Slf4j
public class ClickIdParserTest extends BaseParsersTest {

  private ClickIdParser clickIdParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    clickIdParser = new ClickIdParser();
    yaml = loadTestCasesYaml("ClickIdParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, clickIdParser);
  }
}
