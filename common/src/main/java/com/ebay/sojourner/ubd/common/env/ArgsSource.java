package com.ebay.sojourner.ubd.common.env;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;

public class ArgsSource extends AbstractEnvironment {

  @Override
  public void sourceProps() {
    String cmd = System.getProperty("sun.java.command");
    String[] cmdStr = cmd.split(" ");
    List<String> args = Lists.newArrayList(cmdStr)
        .stream()
        .filter(e -> e.startsWith("--") && e.contains("="))
        .collect(Collectors.toList());

    args.forEach(arg -> {
      String[] kv = arg.substring(2).split("=");
      props.put(kv[0], kv[1]);
    });
  }

  @Override
  public Integer order() {
    return 1;
  }
}
