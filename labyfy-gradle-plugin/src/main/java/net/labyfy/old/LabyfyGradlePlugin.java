package net.labyfy.old;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import groovy.lang.Closure;
import net.labyfy.gradle.LabyfyGradleModule;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.Configurable;
import org.gradle.util.ConfigureUtil;

import javax.annotation.Nonnull;

public class LabyfyGradlePlugin implements Plugin<Project> {

  private static Injector injector;

  public void apply(@Nonnull Project project) {
    injector = this.createInjector(project);
    injector.getInstance(LabyfyGradle.class).apply();
  }

  private Injector createInjector(Project project) {
    return Guice.createInjector(this.createModules(project));
  }

  private Module[] createModules(Project project) {
    return new Module[]{};
  }


  public static class Extension implements Configurable<Extension> {
    private String version;

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public Extension configure(@Nonnull Closure closure) {
      System.out.println("Debug123");
      Extension extension = ConfigureUtil.configureSelf(closure, this);
//      injector.getInstance(LibraryApplier.class).apply();
      return extension;
    }
  }
}
