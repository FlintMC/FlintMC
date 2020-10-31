package net.labyfy.component.settings.serializer;

import com.google.gson.JsonObject;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;

public interface SettingsSerializationHandler<A extends Annotation> {

  // should not append 'name', 'enabled' and 'type' because ...
  void append(JsonObject result, RegisteredSetting setting, A annotation);

}
