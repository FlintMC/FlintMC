package net.labyfy.component.settings.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface JsonSettingsSerializer {

  JsonArray serializeSettings();

  JsonObject serializeSetting(RegisteredSetting setting);

  JsonObject serializeCategories();

  JsonObject serializeCategory(RegisteredCategory category);

  <A extends Annotation> void registerHandler(Class<A> annotationType, SettingsSerializationHandler<A> handler);

  Collection<SettingsSerializationHandler<Annotation>> getHandlers();

  <A extends Annotation> Collection<SettingsSerializationHandler<A>> getHandlers(Class<A> annotationType);

}
