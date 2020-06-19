package net.labyfy.gradle.manifest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.labyfy.gradle.PublishUtils;
import net.labyfy.gradle.library.VersionFetcher;
import net.labyfy.gradle.mapping.MappingDownloader;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.junit.Assert;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PublishLabyfyRelease implements Action<Task> {

  private final Project project;
  private final String minecraftVersion;
  private final String publishToken;
  private final String publishUrl;
  private final String publishVersion;
  private final boolean publishLatest;

  public PublishLabyfyRelease(
      Project project,
      String minecraftVersion,
      String publishToken,
      String publishUrl,
      String publishVersion,
      boolean publishLatest) {
    this.project = project;
    this.minecraftVersion = minecraftVersion;
    this.publishToken = publishToken;
    this.publishUrl = publishUrl;
    this.publishVersion = publishVersion;
    this.publishLatest = publishLatest;
  }

  public void execute(@Nonnull Task task) {
    task.setGroup("labyfy");
    task.dependsOn("jar");
    task.doLast(
        task1 -> {
          VersionFetcher.Version version = VersionFetcher.fetch(this.minecraftVersion).getDetails();
          for (int i = 0; i < version.getArguments().getGame().length; i++) {
            if (version.getArguments().getGame()[i].toString().equals("--version")) {
              version.getArguments().getGame()[i + 1] = this.minecraftVersion;
              break;
            }
          }
          version.setMainClass("net.labyfy.component.launcher.LabyLauncher");
          version.setId("Labyfy-" + this.minecraftVersion);

          Collection<MavenArtifactRepository> mavenArtifactRepositories =
              PublishUtils.collectTransitiveRepositories(project);
          Collection<InstallInstruction> installInstructions = new HashSet<>();
          Collection<VersionFetcher.Version.Library> artifacts = new HashSet<>();

          for (Dependency dependency : PublishUtils.collectTransitiveDependencies(project)) {

            if (dependency instanceof ProjectDependency) {
              Set<File> archives =
                  ((ProjectDependency) dependency)
                      .getDependencyProject()
                      .getConfigurations()
                      .getByName("archives")
                      .getOutgoing()
                      .getArtifacts()
                      .getFiles()
                      .getFiles();
              assert archives.size() == 1 : "archive size must be 1";
              File file = archives.toArray(new File[] {})[0];
              assert file.exists();

              String jarUrl = this.publishUrl + "/package/%s/%s/%s";
              try {
                installInstructions.add(
                    PublishUtils.createManifestDownloadInstruction(
                        dependency,
                        String.format(
                            jarUrl,
                            dependency.getName(),
                            publishVersion,
                            dependency.getName() + "-" + publishVersion + ".jar"),
                        DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)),
                        publishVersion));
              } catch (IOException e) {
                e.printStackTrace();
              }
              artifacts.add(PublishUtils.createLibrary(dependency, publishVersion));

              PublishUtils.publishFile(
                  file,
                  dependency.getName() + "-" + publishVersion + ".jar",
                  dependency.getName(),
                  publishVersion,
                  publishUrl,
                  publishToken);

            } else {
              for (MavenArtifactRepository repository : mavenArtifactRepositories) {
                if (repository.getUrl().getScheme().equalsIgnoreCase("file")) continue;

                URI url = repository.getUrl();

                String jarUrl =
                    String.format(
                        "%s%s/%s/%s/%s-%s.jar",
                        url.toString(),
                        dependency.getGroup().replace('.', '/'),
                        dependency.getName(),
                        dependency.getVersion(),
                        dependency.getName(),
                        dependency.getVersion());
                try {
                  URL jarfile = new URL(jarUrl);
                  InputStream inStream = jarfile.openStream();
                  if (inStream != null) {
                    installInstructions.add(
                        PublishUtils.createManifestDownloadInstruction(
                            dependency,
                            jarUrl,
                            DigestUtils.md5Hex(inStream),
                            dependency.getVersion()));
                    artifacts.add(PublishUtils.createLibrary(dependency, dependency.getVersion()));
                  }
                } catch (Exception ignored) {
                }
              }
            }
          }

          artifacts.addAll(Arrays.asList(version.getLibraries()));
          version.setLibraries(artifacts.toArray(new VersionFetcher.Version.Library[] {}));

          String versionString = version.toString();
          installInstructions.add(
              new ManifestDownload()
                  .setData(
                      new ManifestDownload.Data()
                          .setPath(
                              "versions/Labyfy-"
                                  + this.minecraftVersion
                                  + "/Labyfy-"
                                  + this.minecraftVersion
                                  + ".json")
                          .setMd5(DigestUtils.md5Hex(versionString))
                          .setUrl(
                              this.publishUrl
                                  + "/package/Labyfy-"
                                  + this.minecraftVersion
                                  + "/"
                                  + publishVersion
                                  + "/Labyfy-"
                                  + this.minecraftVersion
                                  + "-"
                                  + publishVersion
                                  + ".json")));

          try {
            HttpGet httpGet = new HttpGet(MappingDownloader.INDEX_URL);
            httpGet.addHeader("User-Agent", "labyfy-gradle-plugin");
            MappingDownloader.Index index =
                new Gson()
                    .fromJson(
                        IOUtils.toString(
                            HttpClients.createDefault().execute(httpGet).getEntity().getContent(),
                            StandardCharsets.UTF_8),
                        MappingDownloader.Index.class);

            MappingDownloader.Index.Mapping mapping = index.getMappings().get(minecraftVersion);
            String mcpBot = mapping.getMcpBot();
            String mcpConfig = mapping.getMcpConfig();

            installInstructions.add(
                new ManifestDownload()
                    .setData(
                        new ManifestDownload.Data()
                            .setUrl("jar:" + mcpBot + "!/methods.csv")
                            .setPath("Labyfy/assets/" + minecraftVersion + "/methods.csv")
                            .setMd5(
                                DigestUtils.md5Hex(
                                    IOUtils.toByteArray(
                                        new URL("jar:" + mcpBot + "!/methods.csv"))))));

            installInstructions.add(
                new ManifestDownload()
                    .setData(
                        new ManifestDownload.Data()
                            .setUrl("jar:" + mcpBot + "!/fields.csv")
                            .setPath("Labyfy/assets/" + minecraftVersion + "/fields.csv")
                            .setMd5(
                                DigestUtils.md5Hex(
                                    IOUtils.toByteArray(
                                        new URL("jar:" + mcpBot + "!/fields.csv"))))));

            installInstructions.add(
                new ManifestDownload()
                    .setData(
                        new ManifestDownload.Data()
                            .setUrl("jar:" + mcpBot + "!/params.csv")
                            .setPath("Labyfy/assets/" + minecraftVersion + "/params.csv")
                            .setMd5(
                                DigestUtils.md5Hex(
                                    IOUtils.toByteArray(
                                        new URL("jar:" + mcpBot + "!/params.csv"))))));

            installInstructions.add(
                new ManifestDownload()
                    .setData(
                        new ManifestDownload.Data()
                            .setUrl("jar:" + mcpConfig + "!/config/joined.tsrg")
                            .setPath("Labyfy/assets/" + minecraftVersion + "/joined.tsrg")
                            .setMd5(
                                DigestUtils.md5Hex(
                                    IOUtils.toByteArray(
                                        new URL("jar:" + mcpConfig + "!/config/joined.tsrg"))))));
          } catch (IOException e) {
            e.printStackTrace();
          }

          Gson gson = new GsonBuilder().setPrettyPrinting().create();

          String manifest =
              gson.toJson(
                  new Manifest()
                      .setDescription("Labyfy base installation")
                      .setName("Labyfy")
                      .setVersion(project.getVersion().toString())
                      .setInstallInstructions(
                          installInstructions.toArray(new InstallInstruction[] {}))
                      .setAuthors("LabyMedia"));

          System.out.println(manifest);

          PublishUtils.publish(
              versionString.getBytes(StandardCharsets.UTF_8),
              "Labyfy-" + this.minecraftVersion + "-" + publishVersion + ".json",
              "Labyfy-" + this.minecraftVersion,
              publishVersion,
              publishUrl,
              publishToken);
          PublishUtils.publish(
              manifest.getBytes(StandardCharsets.UTF_8),
              "manifest.json",
              "Labyfy-" + this.minecraftVersion,
              publishVersion,
              publishUrl,
              publishToken);
          if (publishLatest) {
            PublishUtils.publish(
                manifest.getBytes(StandardCharsets.UTF_8),
                "manifest.json",
                "Labyfy-" + this.minecraftVersion,
                "latest",
                publishUrl,
                publishToken);
          }
        });
  }
}
