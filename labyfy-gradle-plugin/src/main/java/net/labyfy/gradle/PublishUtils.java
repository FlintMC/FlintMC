package net.labyfy.gradle;

import net.labyfy.gradle.library.VersionFetcher;
import net.labyfy.gradle.manifest.InstallInstruction;
import net.labyfy.gradle.manifest.ManifestDownload;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class PublishUtils {

  public static VersionFetcher.Version.Library createLibrary(Dependency dependency, String version) {
    return new VersionFetcher.Version.Library(
        null,
        null,
        null,
        String.format("%s:%s:%s", dependency.getGroup(), dependency.getName(), version),
        null);
  }

  public static InstallInstruction createManifestDownloadInstruction(
      Dependency dependency, String jarUrl, String md5, String version) {
    return new ManifestDownload()
        .setData(
            new ManifestDownload.Data()
                .setUrl(jarUrl)
                .setMd5(md5)
                .setPath(
                    String.format(
                        "libraries/%s/%s/%s/%s",
                        dependency.getGroup().replace('.', '/'),
                        dependency.getName(),
                        version,
                        String.format("%s-%s.jar", dependency.getName(), version))));
  }

  public static Collection<Dependency> collectTransitiveDependencies(Project project) {

    project.getConfigurations().maybeCreate("labyManifest");
    DependencySet projectDependencies =
        project.getConfigurations().getByName("labyManifest").getIncoming().getDependencies();

    Set<Dependency> selectedDependencies = new HashSet<>();

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(ProjectDependency::getDependencyProject)
        .distinct()
        .map(PublishUtils::collectTransitiveDependencies)
        .flatMap(Collection::stream)
        .distinct()
        .filter(Objects::nonNull)
        .forEach(selectedDependencies::add);

    selectedDependencies.addAll(projectDependencies);

    return selectedDependencies;
  }

  public static Collection<MavenArtifactRepository> collectTransitiveRepositories(Project project) {
    project.getConfigurations().maybeCreate("labyManifest");

    DependencySet projectDependencies =
        project.getConfigurations().getByName("labyManifest").getIncoming().getDependencies();

    Map<URI, MavenArtifactRepository> repositories = new HashMap<>();

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(ProjectDependency::getDependencyProject)
        .distinct()
        .map(PublishUtils::collectTransitiveRepositories)
        .flatMap(Collection::stream)
        .distinct()
        .filter(Objects::nonNull)
        .forEach(
            mavenArtifactRepository ->
                repositories.put(mavenArtifactRepository.getUrl(), mavenArtifactRepository));

    projectDependencies.stream()
        .filter(ProjectDependency.class::isInstance)
        .map(ProjectDependency.class::cast)
        .map(ProjectDependency::getDependencyProject)
        .map(Project::getRepositories)
        .flatMap(Collection::stream)
        .filter(MavenArtifactRepository.class::isInstance)
        .map(MavenArtifactRepository.class::cast)
        .distinct()
        .forEach(
            mavenArtifactRepository ->
                repositories.put(mavenArtifactRepository.getUrl(), mavenArtifactRepository));

    return repositories.values();
  }

  public static void publishVersionedFile(
      Project project, File file, String version, String publishUrl, String publishToken) {
    publishFile(
        file,
        replaceLast(file.getName(), (String) project.getVersion(), version),
        project.getName(),
        version,
        publishUrl,
        publishToken);
  }
  
  public static void publishVersioned(Project project, byte[] content,String name, String version, String publishUrl, String publishToken){
    publish(content, replaceLast(name, project.getVersion().toString(), version), project.getName(), version, publishUrl, publishToken);
  }

  public static void publishFile(
      File file,
      String fileName,
      String deployName,
      String deployVersion,
      String publishUrl,
      String publishToken) {
    try {
      publish(
          FileUtils.readFileToByteArray(file),
          fileName,
          deployName,
          deployVersion,
          publishUrl,
          publishToken);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void publish(
      byte[] content,
      String fileName,
      String deployName,
      String deployVersion,
      String publishUrl,
      String publishToken) {
    try {
      HttpEntity entity =
          MultipartEntityBuilder.create()
              .addPart("file", new ByteArrayBody(content, fileName))
              .build();

      HttpPost request =
          new HttpPost(
              new URL(publishUrl + "/publish/" + deployName + "/" + deployVersion).toURI());
      request.setEntity(entity);
      request.addHeader("Authorization", "Bearer " + publishToken);

      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(request);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      System.out.println(
          "published "
              + fileName
              + " to "
              + publishUrl
              + "/publish/"
              + deployName
              + "/"
              + deployVersion);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static String replaceLast(String string, String toReplace, String replacement) {
    int pos = string.lastIndexOf(toReplace);
    if (pos > -1) {
      return string.substring(0, pos) + replacement + string.substring(pos + toReplace.length());
    } else {
      return string;
    }
  }
}
