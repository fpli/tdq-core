package com.ebay.sojourner.ubd.common.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BotAgentMatcher {
  private static final int BOT_MATCH_THRESHOLD = 10000;
  // TODO: Use MinMaxPriorityQueue instead
  // Cache matched agents to reduce comparison.
  private Set<String> botAgents;
  private Filter<String> botAgentFilter;

  /**
   * Construct matcher wish provided browser agents and bot agents.
   *
   * @param browserAgents
   * @param botAgents
   */
  public BotAgentMatcher(Collection<String> browserAgents, Collection<String> botAgents) {
    this.botAgents = new HashSet<String>();
    this.botAgentFilter = new BotAgentFilter(browserAgents, botAgents);
  }

  /**
   * Do bot agent match for specific agent string
   *
   * @param agentString
   * @return true if it's a bot agent or false.
   * @throws IOException
   * @throws InterruptedException
   */
  public boolean match(String agentString) throws IOException, InterruptedException {
    if (botAgents.contains(agentString)) {
      return true;
    } else if (botAgentFilter.filter(agentString)) {
      // Do not cache when cached size larger than threshold
      if (botAgents.size() < BOT_MATCH_THRESHOLD) {
        botAgents.add(agentString);
      }

      return true;
    } else {
      return false;
    }
  }

  /**
   * Clean cached agents which are coming from bots and agent string.
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public void cleanup() throws IOException, InterruptedException {
    // release cached string earilier.
    this.botAgents.clear();
    this.botAgentFilter.cleanup();
  }
}
