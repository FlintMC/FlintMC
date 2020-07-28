package net.labyfy.component.mappings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Singleton
public class McpMappingIndexProvider {

  private final static String INDEX_URL = "https://dl.labymod.net/mappings/index.json";
  private final static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  public McpMappingIndex fetch() throws IOException {
    try (InputStreamReader reader = new InputStreamReader(new URL(INDEX_URL).openStream())) {
      return gson.fromJson(reader, McpMappingIndex.class);
    }
  }

}
