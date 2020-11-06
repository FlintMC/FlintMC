package net.labyfy.internal.component.gamesettings.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;
import net.labyfy.component.config.serialization.ConfigSerializer;
import net.labyfy.component.gamesettings.KeyBinding;

@Singleton
@ConfigSerializer(KeyBinding.class)
public class KeyBindingSerializer implements ConfigSerializationHandler<KeyBinding> {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  public KeyBindingSerializer(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  @Override
  public JsonElement serialize(KeyBinding keyBinding) {
    JsonObject object = new JsonObject();
    object.addProperty("description", keyBinding.getKeyDescription());
    object.addProperty("keyCode", keyBinding.getKeyCode());
    object.addProperty("category", keyBinding.getKeyCategory());
    return object;
  }

  @Override
  public KeyBinding deserialize(JsonElement element) {
    if (!element.isJsonObject()) {
      return null;
    }

    JsonObject object = element.getAsJsonObject();
    return this.keyBindingFactory.create(
        object.get("description").getAsString(),
        object.get("keyCode").getAsInt(),
        object.get("category").getAsString()
    );
  }
}
