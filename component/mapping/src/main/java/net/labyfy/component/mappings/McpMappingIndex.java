package net.labyfy.component.mappings;

import java.util.HashMap;
import java.util.Map;

public class McpMappingIndex {

  private Map<String, Version> mappings = new HashMap<>();

  public Map<String, Version> getMappings() {
    return mappings;
  }

  public static class Version {
    private String mcpBot;
    private String mcpConfig;

    public String getMcpBot() {
      return mcpBot;
    }

    public String getMcpConfig() {
      return mcpConfig;
    }
  }
}
