package net.labyfy.gradle.library;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import net.labyfy.gradle.LabyfyGradlePlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
    this.rootProject.getSubprojects().forEach(project -> {
      project.getRepositories().maven(mavenArtifactRepository -> {
        mavenArtifactRepository.setUrl(System.getenv().getOrDefault("artifactory_contextUrl", project.getProperties().get("artifactory_contextUrl") + "general/"));
      });

      project.getExtensions().create("minecraft", LabyfyGradlePlugin.Extension.class).configured(extension -> {
        try {
          LibraryApplier.this.configured(extension, project);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
    this.registerMinecraftRepository();
  }

  public void configured(LabyfyGradlePlugin.Extension extension, Project project) throws IOException, ParserConfigurationException, TransformerException {
    String version = extension.getVersion();
    if (version == null) return;
    System.out.println("Version " + version + " in project " + project.getName());
    VersionFetcher.Version details = extension.getDetails();

    File repo = new File(project.getGradle().getGradleUserHomeDir(), "caches/labyfy-gradle/repo");
    repo.mkdirs();

    this.generateClient(repo, version, details, project);
    this.generateServer(repo, version, details, project);

    project.getRepositories().maven(mavenArtifactRepository -> {
      mavenArtifactRepository.setUrl(repo.toURI());
      mavenArtifactRepository.metadataSources(MavenArtifactRepository.MetadataSources::gradleMetadata);
    });
  }

  private File generateServer(File repo, String version, VersionFetcher.Version details, Project project) throws
      IOException, TransformerException, ParserConfigurationException {
    return this.createArtifact(repo, project, new URL(details.getDownloads().getServer().getUrl()), "net.minecraft", "server", version);
  }

  private File generateClient(File repo, String version, VersionFetcher.Version details, Project project) throws
      IOException, TransformerException, ParserConfigurationException {
    return this.createArtifact(repo, project, new URL(details.getDownloads().getClient().getUrl()), "net.minecraft", "client", version);
  }

  private File createArtifact(File repo, Project project, URL url, String group, String name, String version) throws
      IOException, ParserConfigurationException, TransformerException {

    String format = String.format("%s/%s/%s/%s-%s", group.replace('.', '/'), name, version, name, version);
    VersionFetcher.Version fetch = VersionFetcher.fetch(version).getDetails();

    File jar = new File(repo, format + ".jar");
    File pom = new File(repo, format + ".pom");
    File gradleMeta = new File(repo, format + ".module");

    if (!jar.exists())
      FileUtils.writeByteArrayToFile(jar, IOUtils.toByteArray(url));

    if (!gradleMeta.exists()) {
      GradleModuleMetaData gradleModuleMetaData = new GradleModuleMetaData()
          .setComponent(new GradleModuleMetaData.Component()
              .setGroup(group)
              .setModule(name)
              .setVersion(version))
          .setVariants(new GradleModuleMetaData.Variant[]{
              new GradleModuleMetaData.Variant()
                  .setName("default")
                  .setAttributes(ImmutableMap.
                      <String, Object>builder()
                      .put("org.gradle.usage", "java-runtime")
                      .put("org.gradle.category", "library")
                      .put("org.gradle.libraryelements", "jar")
                      .put("net.labyfy.requiredeobfuscation", true)
                      .build()
                  ).setFiles(new GradleModuleMetaData.Variant.File[]{
                  new GradleModuleMetaData.Variant.File()
                      .setName(jar.getName())
                      .setUrl(name + "-" + version + ".jar")})
                  .setDependencies(Arrays.stream(fetch.getLibraries())
                  .map(library -> library.getName().split(":"))
                  .map(parameters -> new GradleModuleMetaData.Variant.Dependency()
                      .setGroup(parameters[0])
                      .setModule(parameters[1])
                      .setVersion(ImmutableMap.<String, String>builder()
                          .put("prefers", parameters[2])
                          .build()))
                  .toArray(GradleModuleMetaData.Variant.Dependency[]::new))});

      FileUtils.write(gradleMeta, new GsonBuilder().setPrettyPrinting().create().toJson(gradleModuleMetaData), StandardCharsets.UTF_8);
    }

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
      for (VersionFetcher.Version.Library library : fetch.getLibraries()) {
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
      }
      root.appendChild(dependencies);

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

    Collection<File> files = new ArrayList<>();
    for (VersionFetcher.Version.Library library : fetch.getLibraries()) {
      String[] split = library.getName().split(":");
      File libraryFile = new File(repo, String.format("%s/%s/%s/%s-%s.jar", split[0].replace('.', '/'), split[1], split[2], split[1], split[2]));
      if (!libraryFile.exists()) {
        System.out.println("download " + libraryFile);
        FileUtils.writeByteArrayToFile(libraryFile, IOUtils.toByteArray(url));
      }
      files.add(libraryFile);
    }

    try (JarFile jarFile = new JarFile(jar)) {
      if (jarFile.getJarEntry(".deobfuscated") == null) {
        this.launchArguments.put("--version", version);
        this.libraryRemapper.remap(jar, files);
      }
    }
    return jar;
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
