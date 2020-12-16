package net.flintmc.mcapi.internal.settings.game.annotation;

import com.google.gson.JsonArray;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
@RegisterSettingHandler(ResourcePackSetting.class)
public class ResourcePackSettingHandler implements SettingHandler<ResourcePackSetting> {

  private final ResourcePackProvider provider;

  @Inject
  public ResourcePackSettingHandler(ResourcePackProvider provider) {
    this.provider = provider;
  }

  @Override
  public JsonObject serialize(
      ResourcePackSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    Collection<String> enabledNames = (Collection<String>) currentValue;
    Collection<ResourcePack> enabled = new ArrayList<>();
    Collection<ResourcePack> disabled = new ArrayList<>();
    Collection<ResourcePack> available = this.provider.getAvailable();
    for (ResourcePack pack : available) {
      (enabledNames.contains(pack.getName()) ? enabled : disabled).add(pack);
    }

    object.add("enabledPacks", this.mapPacks(enabled));
    object.add("disabledPacks", this.mapPacks(disabled));

    return object;
  }

  private JsonArray mapPacks(Collection<ResourcePack> packs) {
    JsonArray array = new JsonArray();

    for (ResourcePack pack : packs) {
      JsonObject object = new JsonObject();
      array.add(object);

      object.addProperty("name", pack.getName());
      object.addProperty("description", pack.getDescription());
    }

    return array;
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
