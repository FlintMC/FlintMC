package net.flintmc.mcapi.internal.settings.game.annotation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.resources.pack.ResourcePack;
import net.flintmc.mcapi.resources.pack.ResourcePackProvider;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.game.annotation.ResourcePackSetting;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Singleton
@RegisterSettingHandler(ResourcePackSetting.class)
public class ResourcePackSettingHandler implements SettingHandler<ResourcePackSetting> {

  private final ResourcePackProvider provider;
  private final Gson gson;

  @Inject
  public ResourcePackSettingHandler(ResourcePackProvider provider) {
    this.provider = provider;
    this.gson = new Gson();
  }

  @Override
  public JsonObject serialize(
      ResourcePackSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    Collection<String> enabled = (Collection<String>) currentValue;
    Collection<String> disabled = new HashSet<>();
    Collection<ResourcePack> available = this.provider.getAvailable();
    for (ResourcePack pack : available) {
      if (!enabled.contains(pack.getName())) {
        disabled.add(pack.getName());
      }
    }

    object.add("enabledPacks", this.gson.toJsonTree(enabled));
    object.add("disabledPacks", this.gson.toJsonTree(disabled));

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, ResourcePackSetting annotation) {
    if (!(input instanceof Collection)) {
      return false;
    }

    Collection<String> available =
        provider.getAvailable().stream().map(ResourcePack::getName).collect(Collectors.toSet());

    return available.containsAll((Collection<?>) input);
  }
}
