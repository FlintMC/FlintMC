package net.flintmc.framework.config.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.PostMinecraftRead;
import net.flintmc.framework.config.annotation.PostOpenGLRead;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;

import java.util.Collection;

@Singleton
public class PostMinecraftConfigInitializer {

  private final ConfigGenerator configGenerator;
  private final ConfigAnnotationCollector annotationCollector;

  @Inject
  public PostMinecraftConfigInitializer(
          ConfigGenerator configGenerator, ConfigAnnotationCollector annotationCollector) {
    this.configGenerator = configGenerator;
    this.annotationCollector = annotationCollector;
  }

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  public void readMinecraftConfigs() {
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      Collection<PostMinecraftRead> postMinecraftReads =
              this.annotationCollector.getAllAnnotations(config.getClass(), PostMinecraftRead.class);
      if (!postMinecraftReads.isEmpty()) {
        this.configGenerator.initConfig(config);
      }
    }
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void readOpenGLConfigs() {
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      Collection<PostOpenGLRead> postOpenGLReads =
              this.annotationCollector.getAllAnnotations(config.getClass(), PostOpenGLRead.class);

      if (!postOpenGLReads.isEmpty()) {
        this.configGenerator.initConfig(config);
      }
    }
  }
}
