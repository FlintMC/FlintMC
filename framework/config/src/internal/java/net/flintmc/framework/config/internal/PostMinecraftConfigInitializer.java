package net.flintmc.framework.config.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.PostMinecraftRead;
import net.flintmc.framework.config.annotation.PostOpenGLRead;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

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

  /*@Subscribe(phase = Subscribe.Phase.POST)
  public void readMinecraftConfigs(MinecraftInitializeEvent event) { TODO: Add event class to PostMinecraftRead annotation
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      Collection<PostMinecraftRead> postMinecraftReads =
          this.annotationCollector.getAllAnnotations(config.getClass(), PostMinecraftRead.class);
      if (!postMinecraftReads.isEmpty()) {
        this.configGenerator.initConfig(config);
      }
    }
  }*/

  /*@Subscribe(phase = Subscribe.Phase.POST)
  public void readOpenGLConfigs(OpenGLInitializeEvent event) { TODO: Add event class to PostOpenGLRead annotation
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      Collection<PostOpenGLRead> postOpenGLReads =
          this.annotationCollector.getAllAnnotations(config.getClass(), PostOpenGLRead.class);

      if (!postOpenGLReads.isEmpty()) {
        this.configGenerator.initConfig(config);
      }
    }
  }*/
}
