package net.labyfy.gradle;

import com.google.inject.Guice;
import com.google.inject.Module;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import javax.annotation.Nonnull;

public class LabyfyGradlePlugin implements Plugin<Project> {

  public void apply(@Nonnull Project project) {
    this.createInjector(project);
  }

  private void createInjector(Project project) {
    Guice.createInjector(this.createModules(project)).getInstance(LabyfyGradle.class).apply();
  }

  private Module[] createModules(Project project) {
    return new Module[]{LabyfyGradleModule.create(project, this.createExtension(project))};
  }

  private Extension createExtension(Project project) {
    return project.getExtensions().create("minecraft", Extension.class);
  }

  public static class Extension {
    private String version;

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }
  }
}
