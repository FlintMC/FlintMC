/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.config.internal.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;
import net.flintmc.framework.config.storage.serializer.JsonConfigSerializer;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.metaprogramming.AnnotationMeta;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

@Singleton
@SuppressWarnings("unchecked")
@Implement(ConfigSerializationService.class)
@Service(
    value = ConfigSerializer.class,
    priority = -1 /* has to be called before the ConfigGenerationService */)
public class DefaultConfigSerializationService
    implements ServiceHandler<ConfigSerializer>, ConfigSerializationService {

  private final ClassPool pool;
  private final Logger logger;
  private final Provider<ConfigGenerator> configGenerator;
  private final Provider<JsonConfigSerializer> configSerializer;
  private final Map<Class<?>, ConfigSerializationHandler<?>> handlers;

  @Inject
  private DefaultConfigSerializationService(
      @InjectLogger Logger logger,
      ClassPool pool,
      Provider<ConfigGenerator> configGenerator,
      Provider<JsonConfigSerializer> configSerializer) {
    this.logger = logger;
    this.pool = pool;
    this.configGenerator = configGenerator;
    this.configSerializer = configSerializer;
    this.handlers = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSerializer(Class<?> interfaceType) {
    return this.getSerializer(interfaceType) != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSerializer(CtClass interfaceType) {
    for (Map.Entry<Class<?>, ConfigSerializationHandler<?>> entry : this.handlers.entrySet()) {
      try {
        if (interfaceType.subtypeOf(this.pool.get(entry.getKey().getName()))) {
          return true;
        }
      } catch (NotFoundException e) {
        this.logger.trace(
            "Failed to load CtClass "
                + entry.getKey().getName()
                + " to check if a serializer for "
                + interfaceType.getName()
                + " exists",
            e);
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> ConfigSerializationHandler<T> getSerializer(Class<T> interfaceType) {
    for (Map.Entry<Class<?>, ConfigSerializationHandler<?>> entry : this.handlers.entrySet()) {
      if (entry.getKey().isAssignableFrom(interfaceType)) {
        return (ConfigSerializationHandler<T>) entry.getValue();
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void registerSerializer(
      Class<T> interfaceType, ConfigSerializationHandler<T> handler) {
    this.handlers.put(interfaceType, handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> JsonElement serialize(Class<T> interfaceType, T value) {
    ConfigSerializationHandler<T> handler = this.getSerializer(interfaceType);
    return handler != null ? handler.serialize(value) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T deserialize(Class<T> interfaceType, JsonElement value) {
    ConfigSerializationHandler<T> handler = this.getSerializer(interfaceType);
    return handler != null ? handler.deserialize(value) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonElement serializeWithType(Object value) {
    Class<?> serializable =
        value instanceof ParsedConfig
            ? ((ParsedConfig) value).getConfigClass()
            : this.getSerializableType(value);

    if (serializable == null) {
      serializable = value.getClass();
    }

    JsonObject json = new JsonObject();
    json.addProperty("type", serializable.getName());

    JsonElement serializedValue;
    if (value instanceof ParsedConfig) {
      serializedValue = this.configSerializer.get().serialize((ParsedConfig) value);
    } else {
      ConfigSerializationHandler serializer = this.getSerializer(serializable);
      if (serializer == null) {
        this.logger.trace(
            "No serializer for " + serializable.getName() + " found while serializing object");
        return null;
      }

      serializedValue = serializer.serialize(value);
    }

    json.add("value", serializedValue);

    return json;
  }

  private Class<?> getSerializableType(Object o) {
    for (Class<?> ifc : o.getClass().getInterfaces()) {
      Class<?> serializable = this.getSerializableTypeFromInterface(ifc);
      if (serializable != null) {
        return serializable;
      }
    }
    return null;
  }

  private Class<?> getSerializableTypeFromInterface(Class<?> ifc) {
    for (Class<?> subIfc : ifc.getInterfaces()) {
      Class<?> serializable = this.getSerializableTypeFromInterface(subIfc);
      if (serializable != null) {
        return serializable;
      }
    }

    return this.hasSerializer(ifc) ? ifc : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object deserializeWithType(JsonElement value) {
    if (value.isJsonPrimitive()) {
      return this.deserializePrimitive(value.getAsJsonPrimitive());
    }

    if (value.isJsonObject()) {
      try {
        return this.deserializeObject(value.getAsJsonObject());
      } catch (ClassNotFoundException exception) {
        this.logger.trace("Failed to load a previously defined class for deserializing", exception);
      }
    }

    return null;
  }

  private Object deserializePrimitive(JsonPrimitive primitive) {
    if (primitive.isBoolean()) {
      return primitive.getAsBoolean();
    }

    if (primitive.isNumber()) {
      return primitive.getAsDouble();
    }

    if (primitive.isString()) {
      return primitive.getAsString();
    }

    return null;
  }

  private Object deserializeObject(JsonObject object) throws ClassNotFoundException {
    Class<?> serializable =
        super.getClass().getClassLoader().loadClass(object.get("type").getAsString());
    JsonElement element = object.get("value");

    ParsedConfig config = this.configGenerator.get().createConfigInstance(
        (Class<? extends ParsedConfig>) serializable, false);
    if (config != null) {
      if (element.isJsonObject()) {
        this.configSerializer.get().deserialize(element.getAsJsonObject(), config);
      }

      return config;
    }

    ConfigSerializationHandler<?> serializer = this.getSerializer(serializable);
    if (serializer == null) {
      this.logger.trace(
          "No serializer for " + serializable.getName() + " found while deserializing object");
      return null;
    }

    return serializer.deserialize(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<ConfigSerializer> meta) {
    Class<?> interfaceType = meta.getAnnotation().value();
    CtClass serializerType = (CtClass) meta.getIdentifier().getLocation();

    this.handlers.put(
        interfaceType, InjectionHolder.getInjectedInstance(CtResolver.get(serializerType)));
  }
}
