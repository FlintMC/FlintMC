package net.labyfy.gradle.mapping;

import com.google.gson.Gson;
import net.labyfy.gradle.LabyfyGradlePlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.gradle.api.Project;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipFile;

@Singleton
public class MappingDownloader {

  private final static String INDEX_URL = "https://dl.labymod.net/mappings/index.json";
  private final Project rootProject;

  @Inject
  private MappingDownloader(Project rootProject) {
    this.rootProject = rootProject;
  }

  public void configured(LabyfyGradlePlugin.Extension extension, Project project) throws IOException {
    if (extension.getVersion() == null || extension.getVersion().isEmpty() || !extension.isProvideMappings())
      return;


    if (Files.exists(Paths.get(project.getBuildDir().getPath(), "labyfy/mappings/" + extension.getVersion() + "/extracted/fields.csv"))
        && Files.exists(Paths.get(project.getBuildDir().getPath(), "labyfy/mappings/" + extension.getVersion() + "/extracted/methods.csv"))
        && Files.exists(Paths.get(project.getBuildDir().getPath(), "labyfy/mappings/" + extension.getVersion() + "/extracted/params.csv"))
        && Files.exists(Paths.get(project.getBuildDir().getPath(), "labyfy/mappings/" + extension.getVersion() + "/extracted/joined.tsrg")))
      return;

    CloseableHttpClient httpClient = HttpClients.createDefault();

    HttpGet httpGet = new HttpGet(INDEX_URL);
    httpGet.addHeader("User-Agent", "labyfy-gradle-plugin");
    Index index = new Gson().fromJson(IOUtils.toString(httpClient.execute(httpGet).getEntity().getContent(), StandardCharsets.UTF_8), Index.class);
    if (!index.getMappings().containsKey(extension.getVersion()))
      throw new IllegalArgumentException("Version " + extension.getVersion() + " not found.");

    Index.Mapping mapping = index.getMappings().get(extension.getVersion());

    File mcpBotFile = new File(project.getBuildDir(), "labyfy/mappings/" + extension.getVersion() + "/mcp-bot.zip");

    FileUtils.writeByteArrayToFile(mcpBotFile, IOUtils.toByteArray(new URL(mapping.getMcpBot())));
    ZipFile mcpBotZipFile = new ZipFile(mcpBotFile);
    File extracted = new File(mcpBotFile.getParent(), "extracted");
    extract(mcpBotZipFile, extracted, "methods.csv");
    extract(mcpBotZipFile, extracted, "fields.csv");
    extract(mcpBotZipFile, extracted, "params.csv");

    File mcpConfigFile = new File(project.getBuildDir(), "labyfy/mappings/" + extension.getVersion() + "/mcp-config.zip");
    FileUtils.writeByteArrayToFile(mcpConfigFile, IOUtils.toByteArray(new URL(mapping.getMcpConfig())));
    ZipFile mcpConfigZipFile = new ZipFile(mcpConfigFile);

    extract(mcpConfigZipFile, extracted, "config/joined.tsrg", "joined.tsrg");

    mcpBotZipFile.close();
    mcpConfigZipFile.close();

  }

  public static class Index {

    private Map<String, Mapping> mappings;

    public Map<String, Mapping> getMappings() {
      return mappings;
    }

    public void setMappings(Map<String, Mapping> mappings) {
      this.mappings = mappings;
    }

    public static class Mapping {
      private String mcpBot;
      private String mcpConfig;

      public String getMcpBot() {
        return mcpBot;
      }

      public void setMcpBot(String mcpBot) {
        this.mcpBot = mcpBot;
      }

      public String getMcpConfig() {
        return mcpConfig;
      }

      public void setMcpConfig(String mcpConfig) {
        this.mcpConfig = mcpConfig;
      }
    }
  }

  private void extract(ZipFile zipFile, File extracted, String path) {
    this.extract(zipFile, extracted, path, path);
  }

  private void extract(ZipFile zipFile, File extracted, String path, String output) {
    try (InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(path))) {
      FileUtils.writeByteArrayToFile(new File(extracted, output), IOUtils.toByteArray(inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
