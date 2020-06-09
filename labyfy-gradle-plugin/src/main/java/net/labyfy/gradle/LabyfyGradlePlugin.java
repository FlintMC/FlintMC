package net.labyfy.gradle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import groovy.lang.Closure;
import net.labyfy.gradle.library.LibraryApplier;
import net.labyfy.gradle.library.VersionFetcher;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.Configurable;
import org.gradle.util.ConfigureUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LabyfyGradlePlugin implements Plugin<Project> {

  public static Injector injector;

  public void apply(@Nonnull Project project) {
    this.createInjector(project);
  }

  private void createInjector(Project project) {
    injector = Guice.createInjector(this.createModules(project));
    injector.getInstance(LibraryApplier.class);
  }

  private Module[] createModules(Project project) {
    return new Module[]{LabyfyGradleModule.create(project, this.createExtension(project))};
  }

  private Extension createExtension(Project project) {
    return project.getExtensions().create("minecraft", Extension.class);
  }

  public static class Extension implements Configurable<LabyfyGradlePlugin.Extension> {
    private final Collection<Consumer<Extension>> configured = new HashSet<>();

    private String version;

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public void configured(Consumer<Extension> callable) {
      this.configured.add(callable);
    }

    public Extension configure(@Nonnull Closure closure) {
      Extension extension = ConfigureUtil.configureSelf(closure, this);
      this.configured.forEach(consumer -> consumer.accept(this));
      return extension;
    }

    public VersionFetcher.Version getDetails() {
      return VersionFetcher.fetch(this.version).getDetails();
    }
  }
}
