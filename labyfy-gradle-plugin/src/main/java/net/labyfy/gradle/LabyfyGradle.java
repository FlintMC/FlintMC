package net.labyfy.gradle;

import net.labyfy.gradle.library.LibraryDownloaderTask;
import org.gradle.api.Project;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LabyfyGradle {

  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;
  private final LibraryDownloaderTask libraryDownloaderTask;

  @Inject
  private LabyfyGradle(Project project, LabyfyGradlePlugin.Extension extension, LibraryDownloaderTask libraryDownloaderTask) {
    this.project = project;
    this.extension = extension;
    this.libraryDownloaderTask = libraryDownloaderTask;
  }

  public void apply() {
    this.registerTasks();
  }

  private void registerTasks() {
    project.task("downloadLibraries", this.libraryDownloaderTask);
  }

}
