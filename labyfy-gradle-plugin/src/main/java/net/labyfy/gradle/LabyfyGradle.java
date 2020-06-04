package net.labyfy.gradle;

import net.labyfy.gradle.manifest.PublishLatestRelease;
import net.labyfy.gradle.library.LibraryDownloaderTask;
import org.gradle.api.Project;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LabyfyGradle {

  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;
  private final LibraryDownloaderTask libraryDownloaderTask;
  private final PublishLatestRelease publishLatestRelease;

  @Inject
  private LabyfyGradle(
      Project project,
      LabyfyGradlePlugin.Extension extension,
      LibraryDownloaderTask libraryDownloaderTask,
      PublishLatestRelease publishLatestRelease) {
    this.project = project;
    this.extension = extension;
    this.libraryDownloaderTask = libraryDownloaderTask;
    this.publishLatestRelease = publishLatestRelease;
  }

  public void apply() {
    this.registerTasks();
  }

  private void registerTasks() {
    project.task("downloadLibraries", this.libraryDownloaderTask);
    project.task("publishLatestRelease", this.publishLatestRelease);
  }

}
