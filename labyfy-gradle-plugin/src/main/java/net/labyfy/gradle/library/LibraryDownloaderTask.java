package net.labyfy.gradle.library;

import com.google.common.collect.*;
import com.google.common.io.Files;
import groovy.lang.Singleton;
import net.labyfy.gradle.LabyfyGradlePlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.happy.collections.lists.decorators.SortedList_1x0;

import javax.inject.Inject;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Singleton
public class LibraryDownloaderTask implements Action<Task> {

  private final static String RELATIVE_LIBRARY_PATH = "libraries";

  private final LibraryRemapper libraryRemapper;
  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;
  private VersionFetcher.Version versionDetails;

  @Inject
  private LibraryDownloaderTask(LibraryRemapper libraryRemapper, Project project, LabyfyGradlePlugin.Extension extension) {
    this.libraryRemapper = libraryRemapper;
    this.project = project;
    this.extension = extension;
  }

  public void execute(Task task) {
    task.setGroup("labyfy");
    task.doLast(task1 -> {
      this.validateVersion();
      this.versionDetails = this.fetchVersionDetails();

      File libraryFolder = this.createLibraryFolder();

      this.downloadVersionLibraries(libraryFolder);
      File client = this.downloadClient(libraryFolder);

      if (client != null) {
        download("https://dl.labymod.net/mappings/" + extension.getVersion() + "/methods.csv", new File(project.getProjectDir(), "Labyfy/assets/" + extension.getVersion() + "/methods.csv"));
        download("https://dl.labymod.net/mappings/" + extension.getVersion() + "/fields.csv", new File(project.getProjectDir(), "Labyfy/assets/" + extension.getVersion() + "/fields.csv"));
        download("https://dl.labymod.net/mappings/" + extension.getVersion() + "/joined.tsrg", new File(project.getProjectDir(), "Labyfy/assets/" + extension.getVersion() + "/joined.tsrg"));
        this.deobfuscateClient(client, libraryFolder);
      }
    });
  }

  private void download(String url, File file) {
    try {
      if (file.exists()) return;
      HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
      httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
      httpURLConnection.connect();
      file.getParentFile().mkdirs();
      Files.write(IOUtils.toByteArray(httpURLConnection), file);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private String getUserAgent() {
    return "LabyMod v" + "4" + " on mc" + "1.15.1";
  }

  private void deobfuscateClient(File client, File libraryFolder) {
    this.libraryRemapper.remap(client, libraryFolder);
  }

  private void downloadVersionLibraries(File libraryFolder) {
    Multimap<String, VersionFetcher.Version.Library.Downloads> versions = this.getVersions();

    ImmutableSortedSet.copyOfSorted((SortedSet<String>) versions.keySet()).forEach(artifact -> {
      String[] path = Iterables.getLast(versions.get(artifact)).getArtifact().getPath().split("/");
      String targetVersion = path[path.length - 2];
      for (VersionFetcher.Version.Library.Downloads downloads : versions.get(artifact)) {
        path = downloads.getArtifact().getPath().split("/");
        String version = path[path.length - 2];
        if (targetVersion.equals(version)) {
          downloadArtifact(artifact, version, libraryFolder, downloads.getArtifact().getUrl());
          if (downloads.getClassifiers() != null) {
            for (Map.Entry<String, VersionFetcher.Version.Library.Downloads.Artifact> entry : downloads.getClassifiers().entrySet()) {
              VersionFetcher.Version.Library.Downloads.Artifact classifier = entry.getValue();
              String name = entry.getKey();

              path = classifier.getPath().split("/");
              version = path[path.length - 2];
              downloadArtifact(artifact + "-" + name, version, libraryFolder, classifier.getUrl());
            }
          }
        }
      }
    });
  }

  private File downloadClient(File libraryFolder) {
    return this.downloadArtifact("client", extension.getVersion(), libraryFolder, versionDetails.getDownloads().getClient().getUrl());
  }

  private File downloadArtifact(String artifact, String version, File libraries, String url) {
    try {
      File file = new File(libraries, artifact + "-" + version + ".jar");
      if (file.exists()) {
        System.out.println(" -> skip " + artifact + "-" + version + ".jar to " + libraries.getAbsolutePath() + " url " + url);
        return null;
      }
      System.out.println(" -> download " + artifact + "-" + version + ".jar to " + libraries.getAbsolutePath() + " url " + url);
      FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(new URL(url)));
      return file;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private Multimap<String, VersionFetcher.Version.Library.Downloads> getVersions() {
    Multimap<String, VersionFetcher.Version.Library.Downloads> versions = this.createEmptyVersionMap();

    Arrays.stream(versionDetails.getLibraries()).forEach(library -> {
      String[] path = library.getDownloads().getArtifact().getPath().split("/");
      String artifact = path[path.length - 3];
      versions.put(artifact, library.getDownloads());
    });

    return versions;
  }

  private VersionFetcher.Version fetchVersionDetails() {
    return VersionFetcher.fetch(this.extension.getVersion()).getDetails();
  }


  private Multimap<String, VersionFetcher.Version.Library.Downloads> createEmptyVersionMap() {
    return Multimaps.newListMultimap(new TreeMap<>(String::compareTo),
        () -> SortedList_1x0.of(Lists.newArrayList(), Comparator.comparing(o -> o.getArtifact().getPath())));
  }


  private File createLibraryFolder() {
    File libraries = new File(this.project.getProjectDir(), RELATIVE_LIBRARY_PATH);
    if (!libraries.exists()) libraries.mkdirs();
    return libraries;
  }

  private void validateVersion() {
    assert extension.getVersion() == null : new IllegalArgumentException("minecraft.version must be set1");
  }

}
