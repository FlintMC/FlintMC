package net.labyfy.gradle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import groovy.lang.Closure;
import net.labyfy.gradle.library.LibraryApplier;
import net.labyfy.gradle.library.VersionFetcher;
import net.labyfy.gradle.manifest.PublishLatestRelease;
import net.labyfy.gradle.mapping.MappingDownloader;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.Configurable;
import org.gradle.util.ConfigureUtil;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class LabyfyGradlePlugin implements Plugin<Project> {

  public static Injector injector;

  public void apply(@Nonnull Project rootProject) {
    this.createInjector(rootProject);

    rootProject.subprojects(project -> {
      project.getExtensions().create("labyfy", LabyfyGradlePlugin.Extension.class).configured(extension -> {
        if (extension.getPublishToken() != null && extension.getVersion() != null && !extension.getVersion().isEmpty() && extension.getPublishUrl() != null) {
          project.task("publishLatestRelease", new PublishLatestRelease(project, extension.getVersion(), extension.getPublishToken(), extension.getPublishUrl(), project.getVersion().toString(), true));
          project.task("publishVersionedRelease", new PublishLatestRelease(project, extension.getVersion(), extension.getPublishToken(), extension.getPublishUrl(), project.getVersion().toString(), false));
        }
        try {
          LabyfyGradlePlugin.this.configured(extension, project);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
  }

  private void createInjector(Project rootProject) {
    injector = Guice.createInjector(this.createModules(rootProject));
  }

  private void configured(Extension extension, Project project) throws ParserConfigurationException, TransformerException, IOException {
    injector.getInstance(MappingDownloader.class).configured(extension, project);
    injector.getInstance(LibraryApplier.class).configured(extension, project);
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
    private boolean provideMappings;
    private String publishToken;
    private String publishUrl;

    public String getPublishUrl() {
      return publishUrl;
    }

    public void publishUrl(String publishUrl) {
      this.publishUrl = publishUrl;
    }

    public Collection<Consumer<Extension>> getConfigured() {
      return configured;
    }

    public String getVersion() {
      return version;
    }

    public void publishToken(String publishToken) {
      this.publishToken = publishToken;
    }

    public String getPublishToken() {
      return publishToken;
    }

    public void provideMappings(boolean provideMappings) {
      this.provideMappings = provideMappings;
    }

    public boolean isProvideMappings() {
      return provideMappings;
    }

    public void version(String version) {
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
