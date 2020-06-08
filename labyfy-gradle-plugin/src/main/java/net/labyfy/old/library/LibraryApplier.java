package net.labyfy.old.library;

import com.google.common.collect.*;
import net.labyfy.gradle.library.VersionFetcher;
import net.labyfy.old.LabyfyGradlePlugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.happy.collections.lists.decorators.SortedList_1x0;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Singleton
public class LibraryApplier {

  private final static String RELATIVE_LIBRARY_PATH = "libraries";

  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;

  private VersionFetcher.Version versionDetails;

  @Inject
  private LibraryApplier(Project project, LabyfyGradlePlugin.Extension extension) {
    this.project = project;
    this.extension = extension;
  }

  public void apply() {
    System.out.println("Apply");
    this.validateVersion();
    this.versionDetails = this.fetchVersionDetails();

    this.project.getRepositories().maven(mavenArtifactRepository -> {
      try {
        mavenArtifactRepository.setUrl(new URL("https://libraries.minecraft.net/").toURI());
      } catch (URISyntaxException | MalformedURLException e) {
        e.printStackTrace();
      }
    });

//    this.downloadVersionLibraries();

    this.registerTransformer();

    try {
      this.createURLArtifact(new URL(this.versionDetails.getDownloads().getClient().getUrl()).toURI(), "net.minecraft", "client", "1.15.1");
    } catch (MalformedURLException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private void createURLArtifact(URI uri, String group, String name, String version) {
    this.project.getRepositories().ivy(ivyArtifactRepository -> {
      ivyArtifactRepository.setUrl(uri);
      ivyArtifactRepository.artifactPattern(uri.toASCIIString());
      ivyArtifactRepository.content(repositoryContentDescriptor -> repositoryContentDescriptor.includeVersion(group, name, version));
      ivyArtifactRepository.metadataSources(IvyArtifactRepository.MetadataSources::artifact);
    });
  }

  private void registerTransformer() {
    this.project.getDependencies().registerTransform(LibraryTransformer.class, LibraryTransformer::configure);
    this.project.getDependencies().getAttributesSchema().attribute(LibraryTransformer.DEOBFUSCATED);
    this.project.getDependencies().getArtifactTypes().getByName("jar").getAttributes().attribute(LibraryTransformer.DEOBFUSCATED, false);
    this.project.getConfigurations().getAsMap().values().forEach(configuration -> configuration.getAttributes().attribute(LibraryTransformer.DEOBFUSCATED, true));
  }

  private void downloadVersionLibraries() {
    Multimap<String, VersionFetcher.Version.Library> versions = this.getVersions();

    ImmutableSortedSet.copyOfSorted((SortedSet<String>) versions.keySet()).forEach(artifact -> {
      String[] path = Iterables.getLast(versions.get(artifact)).getDownloads().getArtifact().getPath().split("/");
      String targetVersion = path[path.length - 2];

      for (VersionFetcher.Version.Library library : versions.get(artifact)) {
        VersionFetcher.Version.Library.Downloads downloads = library.getDownloads();


        path = downloads.getArtifact().getPath().split("/");
        String version = path[path.length - 2];
        if (targetVersion.equals(version)) {
          project.getDependencies().add("compile", library.getName());
          if (downloads.getClassifiers() != null) {
            for (String name : downloads.getClassifiers().keySet()) {
              project.getDependencies().add("compile", library.getName() + ":" + name);
            }
          }
        }
      }
    });
  }

  private Multimap<String, VersionFetcher.Version.Library> getVersions() {
    Multimap<String, VersionFetcher.Version.Library> versions = this.createEmptyVersionMap();

    Arrays.stream(versionDetails.getLibraries()).forEach(library -> {
      String[] path = library.getDownloads().getArtifact().getPath().split("/");
      String artifact = path[path.length - 3];
      versions.put(artifact, library);
    });

    return versions;
  }

  private Multimap<String, VersionFetcher.Version.Library> createEmptyVersionMap() {
    return Multimaps.newListMultimap(new TreeMap<>(String::compareTo),
        () -> SortedList_1x0.of(Lists.newArrayList(), Comparator.comparing(o -> o.getDownloads().getArtifact().getPath())));
  }

  private File createLibraryFolder() {
    File libraries = new File(this.project.getProjectDir(), RELATIVE_LIBRARY_PATH);
    if (!libraries.exists()) libraries.mkdirs();
    return libraries;
  }

  private VersionFetcher.Version fetchVersionDetails() {
    return VersionFetcher.fetch(this.extension.getVersion()).getDetails();
  }

  private void validateVersion() {
    if (extension.getVersion() == null) throw new IllegalArgumentException("minecraft.version must be set");
  }
}
