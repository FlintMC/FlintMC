package net.labyfy.gradle.manifest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.labyfy.gradle.PublishUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.internal.impldep.com.google.common.collect.Iterables;
import org.junit.Assert;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PublishPackageRelease implements Action<Task> {

  private final Project project;
  private final String publishVersion;
  private final String publishToken;
  private final String publishUrl;
  private final String version;
  private final boolean latest;

  public PublishPackageRelease(
      Project project,
      String version,
      String publishToken,
      String publishUrl,
      String publishVersion,
      boolean latest) {
    this.project = project;
    this.publishVersion = publishVersion;
    this.publishToken = publishToken;
    this.publishUrl = publishUrl;
    this.version = version;
    this.latest = latest;
  }

  public void execute(@Nonnull Task task) {
    task.setGroup("labyfy");
    task.dependsOn("jar");
    task.doLast(
        task1 -> {
          Set<File> jars =
              this.project.getTasks().getByName("jar").getOutputs().getFiles().getFiles();
          Assert.assertEquals(
              "not exactly one output for task jar in project " + project.getName() + " found.",
              jars.size(),
              1);
          JarFile jar = null;
          File artifactFile = null;
          try {
            artifactFile = Iterables.getOnlyElement(jars);
            jar = new JarFile(artifactFile);
          } catch (IOException e) {
            e.printStackTrace();
          }
          Assert.assertNotNull(jar);
          ZipEntry packageJson = jar.getEntry("package.json");
          Assert.assertNotNull("no package json found in " + jar, packageJson);
          InputStream packageJsonInputStream = null;
          try {
            packageJsonInputStream = jar.getInputStream(packageJson);
          } catch (IOException e) {
            e.printStackTrace();
          }
          LabyPackageDescription labyPackageDescription = null;
          try {
            labyPackageDescription =
                new Gson()
                    .fromJson(
                        IOUtils.toString(packageJsonInputStream, StandardCharsets.UTF_8),
                        LabyPackageDescription.class);
          } catch (IOException e) {
            e.printStackTrace();
          }

          Collection<MavenArtifactRepository> mavenArtifactRepositories =
              PublishUtils.collectTransitiveRepositories(project);
          Collection<InstallInstruction> installInstructions = new HashSet<>();
          Collection<Dependency> dependencies = PublishUtils.collectTransitiveDependencies(project);
          for (Dependency dependency : dependencies) {
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

              System.out.println(
                  "Publish file " + file + " " + publishUrl + " " + dependency.getName());
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
                  }
                } catch (Exception ignored) {
                }
              }
              System.out.println(dependencies);
            }
          }

          try {
            installInstructions.add(
                new ManifestDownload()
                    .setData(
                        new ManifestDownload.Data()
                            .setUrl(
                                String.format(
                                    this.publishUrl + "/package/%s/%s/%s-%s.jar",
                                    project.getName(),
                                    this.publishVersion,
                                    project.getName(),
                                    this.publishVersion))
                            .setMd5(DigestUtils.md5Hex(IOUtils.toByteArray(artifactFile.toURI())))
                            .setPath(
                                "Labyfy/packages/"
                                    + project.getName()
                                    + "-"
                                    + this.publishVersion
                                    + ".jar")));
          } catch (IOException e) {
            e.printStackTrace();
          }

          Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

          String manifest =
              gson.toJson(
                  new Manifest()
                      .setDescription(labyPackageDescription.getDescription())
                      .setName(labyPackageDescription.getName())
                      .setVersion(this.publishVersion)
                      .setInstallInstructions(
                          installInstructions.toArray(new InstallInstruction[] {}))
                      .setAuthors(labyPackageDescription.getAuthors().toArray(new String[] {})));

          try {
            PublishUtils.publishVersioned(project, IOUtils.toByteArray(artifactFile.toURI().toURL()), project.getName() + "-" + this.publishVersion + ".jar", this.publishVersion, this.publishUrl, this.publishToken);
          } catch (IOException e) {
            e.printStackTrace();
          }

          PublishUtils.publishVersioned(
              project,
              manifest.getBytes(StandardCharsets.UTF_8),
              "manifest.json",
              this.publishVersion,
              this.publishUrl,
              this.publishToken);
          if (latest) {

            PublishUtils.publishVersioned(
                project,
                manifest.getBytes(StandardCharsets.UTF_8),
                "manifest.json",
                "latest",
                this.publishUrl,
                this.publishToken);
          }
          System.out.println(manifest);
        });
  }
}
