package com.ebay.sojourner.business.ubd.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@Slf4j
public class ClientIPParserTest extends BaseParsersTest {

  private ClientIPParser clientIPParser;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    clientIPParser = new ClientIPParser();
    yaml = loadTestCasesYaml("ClientIPParserTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, clientIPParser);
  }
}
