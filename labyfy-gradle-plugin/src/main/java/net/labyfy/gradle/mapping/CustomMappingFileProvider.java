package net.labyfy.gradle.mapping;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import net.labyfy.component.mappings.MappingFileProvider;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Singleton
public class CustomMappingFileProvider implements MappingFileProvider {

  @Inject
  private CustomMappingFileProvider() {
  }

  public Map<String, InputStream> getMappings(String version) {
    return ImmutableMap.of(
        "methods.csv", getInputStream("https://dl.labymod.net/mappings/" + version + "/methods.csv", version),
        "fields.csv", getInputStream("https://dl.labymod.net/mappings/" + version + "/fields.csv", version),
        "joined.tsrg", getInputStream("https://dl.labymod.net/mappings/" + version + "/joined.tsrg", version)
    );
  }

  private InputStream getInputStream(String url, String version) {
    try {
      HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
      httpURLConnection.setRequestProperty("User-Agent", getUserAgent(version));
      httpURLConnection.connect();
      return httpURLConnection.getInputStream();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private String getUserAgent(String version) {
    return "Labyfy gradle on mc version: " + version;
  }
}
