package net.labyfy.gradle;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.mappings.MappingFileProvider;
import net.labyfy.gradle.mapping.CustomMappingFileProvider;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

public class LabyfyGradleModule extends AbstractModule {

  private final Project project;

  private LabyfyGradleModule(Project project) {
    this.project = project;
  }

  protected void configure() {
    this.bind(Project.class).toInstance(this.project);
    this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(new HashMap());
    this.bind(MappingFileProvider.class).to(CustomMappingFileProvider.class);
  }


  public static LabyfyGradleModule create(Project project, LabyfyGradlePlugin.Extension extension) {
    return new LabyfyGradleModule(project);
  }

}
