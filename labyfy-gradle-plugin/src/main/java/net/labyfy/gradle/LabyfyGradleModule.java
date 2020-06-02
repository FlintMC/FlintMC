package net.labyfy.gradle;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.gradle.api.Project;

import java.io.File;
import java.util.Map;

public class LabyfyGradleModule extends AbstractModule {

  private final Project project;
  private final LabyfyGradlePlugin.Extension extension;

  private LabyfyGradleModule(Project project, LabyfyGradlePlugin.Extension extension) {
    this.project = project;
    this.extension = extension;
  }

  protected void configure() {
    this.bind(Project.class).toInstance(this.project);
    this.bind(LabyfyGradlePlugin.Extension.class).toInstance(this.extension);
    this.bind(Key.get(File.class, Names.named("labyfyRoot"))).toInstance(new File(project.getProjectDir(), "Labyfy"));
    this.bind(Key.get(Map.class, Names.named("launchArguments")))
        .toProvider(() -> ImmutableMap.<String, String>builder()
            .put("--version", extension.getVersion())
            .build());
  }


  public static LabyfyGradleModule create(Project project, LabyfyGradlePlugin.Extension extension) {
    return new LabyfyGradleModule(project, extension);
  }

}
