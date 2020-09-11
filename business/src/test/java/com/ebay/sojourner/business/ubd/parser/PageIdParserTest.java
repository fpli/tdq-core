package com.ebay.sojourner.business.ubd.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class PageIdParserTest extends BaseParsersTest {

  private PageIdParser pageIdParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    pageIdParser = new PageIdParser();
    yaml = loadTestCasesYaml("PageIdParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, pageIdParser);
  }
}
