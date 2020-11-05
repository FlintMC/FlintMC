package net.labyfy.internal.component.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.annotation.PostMinecraftRead;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;

@Singleton
public class PostMinecraftConfigInitializer {

  private final ConfigGenerator configGenerator;
  private final ConfigStorageProvider storageProvider;

  @Inject
  public PostMinecraftConfigInitializer(ConfigGenerator configGenerator, ConfigStorageProvider storageProvider) {
    this.configGenerator = configGenerator;
    this.storageProvider = storageProvider;
  }

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  public void readConfigs() {
    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      if (config.getClass().isAnnotationPresent(PostMinecraftRead.class)) {
        this.storageProvider.read(config);
      }
    }
  }

}
