package net.labyfy.internal.component.gamesettings.v1_15_2;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.ConfigurationInjector;
import net.labyfy.component.gamesettings.GameSettings;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.transform.hook.Hook;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * NOT FINAL ONLY FOR TESTING
 *
 */
@Singleton
@Implement(value = ConfigurationInjector.class, version = "1.15.2")
public class VersionedConfigurationInjector implements ConfigurationInjector {

  private final GameSettings gameSettings;
  private final Logger logger;

  @Inject
  public VersionedConfigurationInjector(GameSettings gameSettings, Logger logger) {
    this.gameSettings = gameSettings;
    this.logger = logger;
  }

  @Hook(
          className = "net.minecraft.client.GameSettings",
          methodName = "saveOptions"
  )
  @Override
  public void saveConfiguration() {
    this.logger.info("Configuration saved!");
  }

  @Hook(
          executionTime = Hook.ExecutionTime.BEFORE,
          className = "net.minecraft.client.GameSettings",
          methodName = "loadOptions"
  )
  @Override
  public void loadConfiguration() {
    JsonObject object = new JsonObject();

    for (Method declaredMethod : this.gameSettings.getClass().getDeclaredMethods()) {
      if (!declaredMethod.getReturnType().isArray()) {
        String qualifiedName = this.makeQualifiedName(declaredMethod.getName(), declaredMethod.getReturnType().equals(Boolean.TYPE));

        object.add(qualifiedName, null);
      }
    }
    this.logger.info("Configuration loaded!");
  }

  private String makeQualifiedName(String name, boolean type) {
    name = type ? name.substring(2) : name.substring(3);
    name = name.substring(0, 1).toLowerCase() + name.substring(1);

    return name;
  }

}
