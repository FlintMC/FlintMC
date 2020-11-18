package net.flintmc.framework.config.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.PostMinecraftRead;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;

@Singleton
public class PostMinecraftConfigInitializer {

  private final ConfigGenerator configGenerator;

  @Inject
  public PostMinecraftConfigInitializer(ConfigGenerator configGenerator) {
    this.configGenerator = configGenerator;
  }

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  public void readConfigs() {
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      if (config.getClass().isAnnotationPresent(PostMinecraftRead.class)) {
        this.configGenerator.initConfig(config);
      }
    }
  }
}