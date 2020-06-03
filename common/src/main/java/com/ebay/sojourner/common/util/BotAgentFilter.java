package com.ebay.sojourner.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class BotAgentFilter implements Filter<String> {

  public static final String LIKE_STRING = "%";
  public static final int queueCapacity = 100;
  // WHEN browser_actual_type IS NULL AND agent_string LIKE '%AdobeAIR%'
  public static final String ADOBE_AIR = "AdobeAIR";
  private List<String> browserAgentStarts = new ArrayList<String>();
  private List<String> browserAgentEnds = new ArrayList<String>();
  private List<String> browserAgentContains = new ArrayList<String>();
  private List<List<String>> browserAgentMultiContains = new ArrayList<List<String>>();
  private List<String> botAgentStarts = new ArrayList<String>();
  private List<String> botAgentEnds = new ArrayList<String>();
  private List<String> botAgentContains = new ArrayList<String>();
  private List<List<String>> botAgentMultiContains = new ArrayList<List<String>>();

  /**
   * Initial BotAgentFilter with browser agents and bot agents information.
   *
   * @param browserAgents the actual browser agents.
   * @param botAgents the bot agents.
   */
  public BotAgentFilter(Collection<String> browserAgents, Collection<String> botAgents) {
    initAgents(
        browserAgents,
        browserAgentStarts,
        browserAgentEnds,
        browserAgentContains,
        browserAgentMultiContains);
    initAgents(botAgents, botAgentStarts, botAgentEnds, botAgentContains, botAgentMultiContains);
  }

  @Override
  public boolean filter(String agent) throws IOException {
    if (StringUtils.isBlank(agent)) {
      return false;
    }
    if (match(agent, botAgentStarts, botAgentEnds, botAgentContains, botAgentMultiContains)) {
      return true;
    } else {
      return agent.contains(ADOBE_AIR)
          && !match(
          agent,
          browserAgentStarts,
          browserAgentEnds,
          browserAgentContains,
          browserAgentMultiContains)
          && !matchMulti(agent);
    }
  }

  @Override
  public void cleanup() throws IOException, InterruptedException {
    browserAgentStarts.clear();
    browserAgentEnds.clear();
    browserAgentContains.clear();
    botAgentStarts.clear();
    botAgentEnds.clear();
    botAgentContains.clear();
  }

  /**
   *
   */
  private boolean match(
      String agent,
      List<String> agentStarts,
      List<String> agentEnds,
      List<String> agentContains,
      List<List<String>> agentMultiContains) {
    for (String agentStart : agentStarts) {
      if (agent.startsWith(agentStart)) {
        return true;
      }
    }

    for (String agentEnd : agentEnds) {
      if (agent.endsWith(agentEnd)) {
        return true;
      }
    }

    for (String agentContain : agentContains) {
      if (agent.contains(agentContain)) {
        return true;
      }
    }

    for (List<String> agentMultiContain : agentMultiContains) {
      boolean multiContain = true;
      for (String agentContain : agentMultiContain) {
        if (!agent.contains(agentContain)) {
          multiContain = false;
        }
      }

      if (multiContain) {
        return true;
      }
    }

    return false;
  }

  private void initAgents(
      Collection<String> agents,
      List<String> agentStarts,
      List<String> agentEnds,
      List<String> agentContains,
      List<List<String>> agentMultiContains) {
    for (String agent : agents) {
      boolean start = agent.startsWith(LIKE_STRING);
      boolean end = agent.endsWith(LIKE_STRING);
      if (start) {
        if (end) {
          String containString = agent.substring(1, agent.length() - 1);
          if (containString.indexOf(LIKE_STRING) > -1) {
            String[] contains = containString.split(LIKE_STRING);
            agentMultiContains.add(Arrays.asList(contains));
          } else {
            agentContains.add(containString);
          }
        } else {
          agentStarts.add(agent.substring(1));
        }
      } else if (end) {
        agentEnds.add(agent.substring(0, agent.length() - 1));
      }
    }
  }

  private boolean matchMulti(String agent) {
    if (agent.contains("Sleipnir")
        && (agent.contains("Mobile")
        || agent.contains("iPhone")
        || agent.contains("iPad")
        || agent.contains("iPod"))) {
      return true;
    } else if (agent.contains("Chrome/") && !agent.contains("chromeframe")) {
      return true;
    } else {
      return agent.startsWith("Dalvik/") && !agent.contains("Safari");
    }
  }
}
