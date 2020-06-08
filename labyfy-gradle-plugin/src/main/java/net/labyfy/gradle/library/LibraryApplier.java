package net.labyfy.gradle.library;

import com.google.common.collect.*;
import net.labyfy.gradle.LabyfyGradlePlugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ivy.IvyModuleDescriptor;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeCompatibilityRule;
import org.gradle.api.attributes.Usage;
import org.gradle.api.attributes.java.TargetJvmVersion;
import org.gradle.api.model.ObjectFactory;
import org.gradle.model.Mutate;
import org.happy.collections.lists.decorators.SortedList_1x0;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeMap;

@Singleton
public class LibraryApplier {

  private final Project rootProject;

  @Inject
  private LibraryApplier(Project rootProject) {
    this.rootProject = rootProject;
    this.registerMinecraftRepository();
  }

  public void configured(LabyfyGradlePlugin.Extension extension, Project project) throws MalformedURLException, URISyntaxException {
    String version = extension.getVersion();
    if (version == null) return;
    System.out.println("Version " + version + " in project " + project.getName());
    VersionFetcher.Version details = extension.getDetails();

    this.downloadVersionLibraries(details, project);
    this.downloadClient(version, details, project);
    this.downloadServer(version, details, project);

    this.registerDependencyTransformer(project);
  }


  private void downloadServer(String version, VersionFetcher.Version details, Project project) throws MalformedURLException, URISyntaxException {
    this.createURLArtifact(project, new URL(details.getDownloads().getServer().getUrl()).toURI(), "net.minecraft", "server", version);
  }

  private void downloadClient(String version, VersionFetcher.Version details, Project project) throws MalformedURLException, URISyntaxException {
    this.createURLArtifact(project, new URL(details.getDownloads().getClient().getUrl()).toURI(), "net.minecraft", "client", version);
  }

  private void registerDependencyTransformer(Project project) {
    project.getDependencies().registerTransform(LibraryJarDeobfuscationTransformer.class, spec -> LibraryJarDeobfuscationTransformer.configure(spec, project.getExtensions().getByType(LabyfyGradlePlugin.Extension.class).getVersion()));
    project.getDependencies().getAttributesSchema().attribute(LibraryJarDeobfuscationTransformer.DEOBFUSCATED);
    project.getDependencies().getArtifactTypes().getByName("jar").getAttributes().attribute(LibraryJarDeobfuscationTransformer.DEOBFUSCATED, false);
    project.getDependencies().getComponents().withModule("net.minecraft:client", MinecraftComponentMetadataRule.class);
    project.getConfigurations().getAsMap().values().forEach(configuration -> configuration.getAttributes().attribute(LibraryJarDeobfuscationTransformer.DEOBFUSCATED, true));

    project.getDependencies().registerTransform(LibraryIvyToJarNameTransformer.class, LibraryIvyToJarNameTransformer::configure);
    project.getDependencies().getAttributesSchema().attribute(LibraryIvyToJarNameTransformer.RENAMED);
    project.getDependencies().getArtifactTypes().getByName("jar").getAttributes().attribute(LibraryIvyToJarNameTransformer.RENAMED, false);
    project.getConfigurations().getAsMap().values().forEach(configuration -> configuration.getAttributes().attribute(LibraryIvyToJarNameTransformer.RENAMED, true));
  }

  private void createURLArtifact(Project project, URI uri, String group, String name, String version) {
    project.getRepositories().ivy(ivyArtifactRepository -> {
      ivyArtifactRepository.setUrl(uri);
      ivyArtifactRepository.artifactPattern(uri.toASCIIString());
      ivyArtifactRepository.content(repositoryContentDescriptor -> repositoryContentDescriptor.includeVersion(group, name, version));
      ivyArtifactRepository.metadataSources(IvyArtifactRepository.MetadataSources::artifact);
    });

    project.getDependencies().add("compile", String.format("%s:%s:%s", group, name, version));
  }


  private void downloadVersionLibraries(VersionFetcher.Version details, Project project) {
    Multimap<String, VersionFetcher.Version.Library> libraries = this.getLibraries(details);

    ImmutableSortedSet.copyOfSorted((SortedSet<String>) libraries.keySet()).forEach(artifact -> {
      String[] path = Iterables.getLast(libraries.get(artifact)).getDownloads().getArtifact().getPath().split("/");
      String targetVersion = path[path.length - 2];

      for (VersionFetcher.Version.Library library : libraries.get(artifact)) {
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

  private Multimap<String, VersionFetcher.Version.Library> getLibraries(VersionFetcher.Version details) {
    Multimap<String, VersionFetcher.Version.Library> versions = this.createEmptyVersionMap();

    Arrays.stream(details.getLibraries()).forEach(library -> {
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

  public static abstract class MinecraftComponentMetadataRule implements ComponentMetadataRule {
    @Inject
    public MinecraftComponentMetadataRule() {
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    public void execute(ComponentMetadataContext context) {
      // context.getDetails().getAttributes().attribute(LibraryJarDeobfuscationTransformer.DEOBFUSCATED, true);
      context.getDetails().allVariants((variant) -> {
        for (Attribute<?> key : variant.getAttributes().keySet()) {
          System.out.println(key.getName() + " = " + variant.getAttributes().getAttribute(key).toString());
        }

        variant.attributes((attributes) -> {
          attributes.attribute(LibraryIvyToJarNameTransformer.RENAMED, true);
          attributes.attribute(LibraryJarDeobfuscationTransformer.DEOBFUSCATED, true);
        });
      });
    }
  }
}
