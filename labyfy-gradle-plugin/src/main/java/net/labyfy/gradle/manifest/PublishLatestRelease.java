package net.labyfy.gradle.manifest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.labyfy.gradle.library.VersionFetcher;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PublishLatestRelease implements Action<Task> {

  private final Project project;
  private final String minecraftVersion;
  private final String publishToken;
  private final String publishUrl;
  private final String publishVersion;
  private final boolean publishLatest;

  public PublishLatestRelease(Project project,
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
    task.doLast(task1 -> {

      VersionFetcher.Version version = VersionFetcher.fetch(this.minecraftVersion).getDetails();
      for (int i = 0; i < version.getArguments().getGame().length; i++) {
        if (version.getArguments().getGame()[i].toString().equals("--minecraftVersion")) {
          version.getArguments().getGame()[i + 1] = this.minecraftVersion;
          break;
        }
      }
      version.setMainClass("net.labyfy.component.launcher.LabyLauncher");
      version.setId("Labyfy-" + this.minecraftVersion);

      Collection<MavenArtifactRepository> mavenArtifactRepositories = collectTransitiveRepositories(project);
      Collection<InstallInstruction> installInstructions = new HashSet<>();
      Collection<VersionFetcher.Version.Library> artifacts = new HashSet<>();

      for (Dependency dependency : this.collectTransitiveDependencies()) {

        if (dependency instanceof ProjectDependency) {
          Set<File> archives = ((ProjectDependency) dependency).getDependencyProject().getConfigurations().getByName("archives").getOutgoing().getArtifacts().getFiles().getFiles();
          assert archives.size() == 1 : "archive size must be 1";
          File file = archives.toArray(new File[]{})[0];
          assert file.exists();

          String jarUrl = this.publishUrl + "/package/%s/%s/%s";
          try {
            installInstructions.add(this.createManifestDownloadInstruction(dependency,
                String.format(
                    jarUrl,
                    dependency.getName(),
                    publishVersion,
                    dependency.getName() + "-" + publishVersion + ".jar"
                ),
                DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)),
                publishVersion));
          } catch (IOException e) {
            e.printStackTrace();
          }
          artifacts.add(this.createLibrary(dependency, publishVersion));

          publishFile(file, dependency.getName() + "-" + publishVersion + ".jar", dependency.getName(), publishVersion);

        } else {
          for (MavenArtifactRepository repository : mavenArtifactRepositories) {
            if (repository.getUrl().getScheme().equalsIgnoreCase("file"))
              continue;

            URI url = repository.getUrl();

            String jarUrl = String.format("%s%s/%s/%s/%s-%s.jar", url.toString(),
                dependency.getGroup().replace('.', '/'), dependency.getName(), dependency.getVersion(),
                dependency.getName(), dependency.getVersion());
            try {
              URL jarfile = new URL(jarUrl);
              InputStream inStream = jarfile.openStream();
              if (inStream != null) {
                installInstructions.add(this.createManifestDownloadInstruction(dependency, jarUrl, DigestUtils.md5Hex(inStream), dependency.getVersion()));
                artifacts.add(this.createLibrary(dependency, dependency.getVersion()));
              }
            } catch (Exception ignored) {
            }
          }
        }
      }

      artifacts.addAll(Arrays.asList(version.getSortedLibraries()));
      version.setLibraries(artifacts.toArray(new VersionFetcher.Version.Library[]{}));

      String versionString = version.toString();
      installInstructions.add(
          new ManifestDownload()
              .setData(new ManifestDownload.Data()
                  .setPath("versions/Labyfy-" + this.minecraftVersion + "/Labyfy-" + this.minecraftVersion + ".json")
                  .setMd5(DigestUtils.md5Hex(versionString))
                  .setUrl(this.publishUrl + "/package/Labyfy-" + this.minecraftVersion + "/" + publishVersion + "/Labyfy-" + this.minecraftVersion + "-" + publishVersion + ".json")
              ));


      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      String manifest = gson.toJson(new Manifest()
          .setDescription("Labyfy base installation")
          .setName("Labyfy")
          .setVersion(project.getVersion().toString())
          .setInstallInstructions(installInstructions.toArray(new InstallInstruction[]{}))
          .setAuthors("LabyMedia"));

      publish(versionString.getBytes(StandardCharsets.UTF_8), "Labyfy-" + this.minecraftVersion + "-" + publishVersion + ".json", "Labyfy-" + this.minecraftVersion, publishVersion);
      publish(manifest.getBytes(StandardCharsets.UTF_8), "manifest.json", "Labyfy-" + this.minecraftVersion, publishVersion);
      if(publishLatest){
        publish(manifest.getBytes(StandardCharsets.UTF_8), "manifest.json", "Labyfy-" + this.minecraftVersion, "latest");
      }


    });
  }

  private VersionFetcher.Version.Library createLibrary(Dependency dependency, String version) {
    return new VersionFetcher.Version.Library(null, null, null, String.format("%s:%s:%s", dependency.getGroup(), dependency.getName(), version), null);
  }


  private InstallInstruction createManifestDownloadInstruction(Dependency dependency, String jarUrl, String md5, String version) {
    return new ManifestDownload()
        .setData(new ManifestDownload.Data()
            .setUrl(jarUrl)
            .setMd5(md5)
            .setPath(
                String.format(
                    "libraries/%s/%s/%s/%s",
                    dependency.getGroup().replace('.', '/'),
                    dependency.getName(),
                    version,
                    String.format("%s-%s.jar",
                        dependency.getName(),
                        version
                    ))));
  }

  private Collection<Dependency> collectTransitiveDependencies() {
    return this.collectTransitiveDependencies(this.project);
  }

  private Collection<Dependency> collectTransitiveDependencies(Project project) {

    DependencySet projectDependencies = project.getConfigurations()
        .getByName("labyManifest")
        .getIncoming()
        .getDependencies();

    Set<Dependency> selectedDependencies = new HashSet<>();

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(ProjectDependency::getDependencyProject)
        .distinct()
        .map(this::collectTransitiveDependencies)
        .flatMap(Collection::stream)
        .distinct()
        .filter(Objects::nonNull)
        .forEach(selectedDependencies::add);

    selectedDependencies.addAll(projectDependencies);

    return selectedDependencies;
  }

  private Collection<MavenArtifactRepository> collectTransitiveRepositories(Project project) {
    DependencySet projectDependencies = project.getConfigurations()
        .getByName("labyManifest")
        .getIncoming()
        .getDependencies();

    Map<URI, MavenArtifactRepository> repositories = new HashMap<>();

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(projectDependency -> projectDependency.getDependencyProject())
        .distinct()
        .map(this::collectTransitiveRepositories)
        .flatMap(Collection::stream)
        .distinct()
        .filter(Objects::nonNull)
        .forEach(mavenArtifactRepository -> repositories.put(mavenArtifactRepository.getUrl(), mavenArtifactRepository));

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(ProjectDependency::getDependencyProject)
        .map(Project::getRepositories)
        .flatMap(Collection::stream)
        .filter(MavenArtifactRepository.class::isInstance)
        .map(MavenArtifactRepository.class::cast)
        .distinct()
        .forEach(mavenArtifactRepository -> repositories.put(mavenArtifactRepository.getUrl(), mavenArtifactRepository));

    return repositories.values();
  }


  private void publishVersionedFile(Project project, File file, String version) {
    publishFile(file, replaceLast(file.getName(), (String) project.getVersion(), version), project.getName(), version);
  }

  private void publishFile(File file, String fileName, String deployName, String deployVersion) {
    try {
      publish(FileUtils.readFileToByteArray(file), fileName, deployName, deployVersion);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void publish(byte[] content, String fileName, String deployName, String deployVersion) {
    try {
      HttpEntity entity = MultipartEntityBuilder.create()
          .addPart("file", new ByteArrayBody(content, fileName))
          .build();

      HttpPost request = new HttpPost(new URL(this.publishUrl + "/publish/" + deployName + "/" + deployVersion).toURI());
      request.setEntity(entity);
      request.addHeader("Authorization", "Bearer " + this.publishToken);

      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(request);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      System.out.println("published " + fileName + " to " + this.publishUrl + "/publish/" + deployName + "/" + deployVersion);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private String replaceLast(String string, String toReplace, String replacement) {
    int pos = string.lastIndexOf(toReplace);
    if (pos > -1) {
      return string.substring(0, pos) + replacement + string.substring(pos + toReplace.length());
    } else {
      return string;
    }
  }

}