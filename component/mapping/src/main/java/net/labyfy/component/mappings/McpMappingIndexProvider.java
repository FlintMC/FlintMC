package net.labyfy.component.mappings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class McpMappingIndexProvider {

  private final static String INDEX_URL = "https://dl.labymod.net/mappings/index.json";
  private final static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
  private final static HttpClient httpClient = HttpClients.createDefault();

  public McpMappingIndex fetch() throws IOException {
    HttpResponse response = httpClient.execute(new HttpGet(INDEX_URL));
    return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8), McpMappingIndex.class);
  }

}
