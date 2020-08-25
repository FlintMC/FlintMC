package net.labyfy.component.mappings;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Singleton
public class McpMappingIndexProvider {
  private final static String INDEX_URL = "https://dl.labymod.net/mappings/index_new.json";
  private final static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  /**
   * Fetch information about Minecraft mappings.
   *
   * @return A version &lt;-&gt; mapping information map.
   * @throws IOException If the mapping information could not be retrieved.
   */
  public Map<String, Version> fetch() throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(INDEX_URL).openConnection();
    connection.setRequestProperty("User-Agent", "Labyfy/0.2.2 (+https://www.labymod.net/)");
    connection.connect();

    try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
      return gson.fromJson(reader, new TypeToken<Map<String, Version>>() {}.getType());
    }
  }
}
