package net.labyfy.old;

import net.labyfy.gradle.library.LibraryDownloaderTask;
import net.labyfy.gradle.manifest.PublishLatestRelease;
import net.labyfy.old.library.LibraryApplier;
import org.gradle.api.Project;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LabyfyGradle {

  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;
  private final LibraryDownloaderTask libraryDownloaderTask;
  private final PublishLatestRelease publishLatestRelease;
  private final LibraryApplier libraryApplier;

  @Inject
  private LabyfyGradle(
      Project project,
      LabyfyGradlePlugin.Extension extension,
      LibraryDownloaderTask libraryDownloaderTask,
      PublishLatestRelease publishLatestRelease,
      LibraryApplier libraryApplier) {
    this.project = project;
    this.extension = extension;
    this.libraryDownloaderTask = libraryDownloaderTask;
    this.publishLatestRelease = publishLatestRelease;
    this.libraryApplier = libraryApplier;
  }

  public void apply() {
    this.registerTasks();
//    this.libraryApplier.apply();
  }

  private void registerTasks() {
    project.task("downloadLibraries", this.libraryDownloaderTask);
    project.task("publishLatestRelease", this.publishLatestRelease);
  }

}
