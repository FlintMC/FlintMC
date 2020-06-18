package net.labyfy.gradle.library;

import com.google.common.io.Files;
import net.labyfy.gradle.LabyfyGradlePlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.jar.JarFile;

@Singleton
public class LibraryApplier {

  private final Project rootProject;
  private final Map<String, String> launchArguments;
  private final LibraryRemapper libraryRemapper;

  @Inject
  private LibraryApplier(Project rootProject, LibraryRemapper libraryRemapper, @Named("launchArguments") Map launchArguments) {
    this.rootProject = rootProject;
    this.libraryRemapper = libraryRemapper;
    this.launchArguments = launchArguments;
    this.rootProject.subprojects((project) -> {
      project.afterEvaluate((project1) -> {
        project.getRepositories().maven(mavenArtifactRepository -> {
          mavenArtifactRepository.setUrl(System.getenv().getOrDefault("artifactory_contextUrl", project.getProperties().get("artifactory_contextUrl") + "general/"));
        });
      });
    });
    this.registerMinecraftRepository();
  }

  public void configured(LabyfyGradlePlugin.Extension extension, Project project) throws IOException, ParserConfigurationException, TransformerException {
    String version = extension.getVersion();
    if (version == null) return;
    VersionFetcher.Version details = extension.getDetails();

    File repo = new File(project.getGradle().getGradleUserHomeDir(), "caches/labyfy-gradle/repo");
    repo.mkdirs();
    VersionFetcher.Version fetch = VersionFetcher.fetch(version).getDetails();

    Collection<File> dependencies = this.collectAndDownloadLibraries(fetch, repo);

    File server = this.generateServer(repo, version, details);
    File client = this.generateClient(repo, version, details);

    File deobfuscatedServer = new File(server.getParent(), "server-" + version + ".jar");
    File deobfuscatedClient = new File(client.getParent(), "client-" + version + ".jar");

    if(!deobfuscatedServer.exists()){
      Files.copy(server, deobfuscatedServer);
    }

    if(!deobfuscatedClient.exists()){
      Files.copy(client, deobfuscatedClient);
    }


    dependencies.add(server);
    this.deobfuscate(deobfuscatedClient, dependencies, version);

    dependencies.remove(server);
    this.deobfuscate(deobfuscatedServer, dependencies, version);

    project.getRepositories().maven(mavenArtifactRepository -> {
      mavenArtifactRepository.setUrl(repo.toURI());
    });
  }

  private void deobfuscate(File file, Collection<File> dependencies, String version) {
    try (JarFile jarFile = new JarFile(file)) {
      if (jarFile.getJarEntry(".deobfuscated") == null) {
        this.launchArguments.put("--version", version);
        this.libraryRemapper.remap(file, dependencies);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File generateServer(File repo, String version, VersionFetcher.Version details) throws
      IOException, TransformerException, ParserConfigurationException {
    return this.createArtifact(repo, new URL(details.getDownloads().getServer().getUrl()), "net.minecraft", "server", version, details, "server-" + version + "-obfuscated.jar");
  }

  private File generateClient(File repo, String version, VersionFetcher.Version details) throws
      IOException, TransformerException, ParserConfigurationException {
    return this.createArtifact(repo, new URL(details.getDownloads().getClient().getUrl()), "net.minecraft", "client", version, details, "client-" + version + "-obfuscated.jar");
  }

  private File createArtifact(File repo, URL url, String group, String name, String version, VersionFetcher.Version details, String nameOverride) throws
      IOException, ParserConfigurationException, TransformerException {

    String format = String.format("%s/%s/%s/%s-%s", group.replace('.', '/'), name, version, name, version);

    File jar = new File(new File(repo, format + ".jar").getParentFile(), nameOverride);
    File pom = new File(repo, format + ".pom");

    if (!jar.exists())
      FileUtils.writeByteArrayToFile(jar, IOUtils.toByteArray(url));

    if (!pom.exists()) {

      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      document.setXmlStandalone(true);
      document.setXmlVersion("1.0");

      Element root = document.createElement("project");
      root.setAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");
      root.setAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
      root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      document.appendChild(root);

      Element modelVersionElement = document.createElement("modelVersion");
      modelVersionElement.appendChild(document.createTextNode("4.0.0"));
      root.appendChild(modelVersionElement);

      Element groupIdElement = document.createElement("groupId");
      groupIdElement.appendChild(document.createTextNode(group));
      root.appendChild(groupIdElement);

      Element artifactIdElement = document.createElement("artifactId");
      artifactIdElement.appendChild(document.createTextNode(name));
      root.appendChild(artifactIdElement);

      Element versionElement = document.createElement("version");
      versionElement.appendChild(document.createTextNode(version));
      root.appendChild(versionElement);


      Element dependencies = document.createElement("dependencies");
      for (VersionFetcher.Version.Library library : details.getSortedLibraries()) {
        Element dependency = document.createElement("dependency");
        Element groupIdDependencyElement = document.createElement("groupId");
        Element artifactIdDependencyElement = document.createElement("artifactId");
        Element versionDependencyElement = document.createElement("version");
        Element scopeDependencyElement = document.createElement("scope");

        //TODO possibly parse identifier
        String[] split = library.getName().split(":");

        groupIdDependencyElement.appendChild(document.createTextNode(split[0]));
        artifactIdDependencyElement.appendChild(document.createTextNode(split[1]));
        versionDependencyElement.appendChild(document.createTextNode(split[2]));
        scopeDependencyElement.appendChild(document.createTextNode("compile"));

        dependency.appendChild(groupIdDependencyElement);
        dependency.appendChild(artifactIdDependencyElement);
        dependency.appendChild(versionDependencyElement);
        dependency.appendChild(scopeDependencyElement);

        dependencies.appendChild(dependency);

        if (library.getDownloads().getClassifiers() != null) {
          for (Map.Entry<String, VersionFetcher.Version.Library.Downloads.Artifact> entry : library.getDownloads().getClassifiers().entrySet()) {
            Element classifierDependency = document.createElement("dependency");
            Element groupIdClassifierDependencyElement = document.createElement("groupId");
            Element artifactIdClassifierDependencyElement = document.createElement("artifactId");
            Element versionClassifierDependencyElement = document.createElement("version");
            Element scopeClassifierDependencyElement = document.createElement("scope");
            Element classifierDependencyElement = document.createElement("classifier");

            //TODO possibly parse identifier
            String[] splitClassifier = library.getName().split(":");

            groupIdClassifierDependencyElement.appendChild(document.createTextNode(splitClassifier[0]));
            artifactIdClassifierDependencyElement.appendChild(document.createTextNode(splitClassifier[1]));
            versionClassifierDependencyElement.appendChild(document.createTextNode(splitClassifier[2]));
            scopeClassifierDependencyElement.appendChild(document.createTextNode("compile"));
            classifierDependencyElement.appendChild(document.createTextNode(entry.getKey()));

            classifierDependency.appendChild(groupIdClassifierDependencyElement);
            classifierDependency.appendChild(artifactIdClassifierDependencyElement);
            classifierDependency.appendChild(versionClassifierDependencyElement);
            classifierDependency.appendChild(scopeClassifierDependencyElement);
            classifierDependency.appendChild(classifierDependencyElement);

            dependencies.appendChild(classifierDependency);
          }
        }

      }
      root.appendChild(dependencies);
      pom.getParentFile().mkdirs();

      try (FileOutputStream fileOutputStream = new FileOutputStream(pom)) {
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(fileOutputStream);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(domSource, streamResult);
        fileOutputStream.flush();
      }
    }

    return jar;
  }

  private Collection<File> collectAndDownloadLibraries(VersionFetcher.Version version, File repo) throws IOException {
    Collection<File> files = new ArrayList<>();
    for (VersionFetcher.Version.Library library : version.getSortedLibraries()) {
      String[] split = library.getName().split(":");
      File libraryFile = new File(repo, String.format("%s/%s/%s/%s-%s.jar", split[0].replace('.', '/'), split[1], split[2], split[1], split[2]));
      if (!libraryFile.exists()) {
        System.out.println("download " + libraryFile);
        FileUtils.writeByteArrayToFile(libraryFile, IOUtils.toByteArray(new URL(library.getDownloads().getArtifact().getUrl())));
      }
      files.add(libraryFile);

      if (library.getDownloads().getClassifiers() != null) {
        for (Map.Entry<String, VersionFetcher.Version.Library.Downloads.Artifact> entry : library.getDownloads().getClassifiers().entrySet()) {
          File classifierFile = new File(libraryFile.getParent(), FilenameUtils.removeExtension(libraryFile.getName()) + "-" + entry.getKey() + ".jar");
          if (!classifierFile.exists()) {
            FileUtils.writeByteArrayToFile(classifierFile, IOUtils.toByteArray(new URL(entry.getValue().getUrl())));
          }
          files.add(classifierFile);
        }
      }
    }
    return files;
  }

  private void registerMinecraftRepository() {
    this.rootProject.getAllprojects().forEach(project -> {
      project.getRepositories().maven(mavenArtifactRepository -> {
        try {
          mavenArtifactRepository.setUrl(new URL("https://libraries.minecraft.net/").toURI());
        } catch (URISyntaxException | MalformedURLException e) {
          e.printStackTrace();
        }
      });
    });
  }
}
