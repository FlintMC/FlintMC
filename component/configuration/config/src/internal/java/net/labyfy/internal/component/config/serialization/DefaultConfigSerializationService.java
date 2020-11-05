package net.labyfy.internal.component.config.serialization;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;
import net.labyfy.component.config.serialization.ConfigSerializationService;
import net.labyfy.component.config.serialization.ConfigSerializer;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(ConfigSerializer.class) // has to be called before the ConfigGenerationService
@Implement(ConfigSerializationService.class)
public class DefaultConfigSerializationService implements ServiceHandler<ConfigSerializer>, ConfigSerializationService {

  private final ClassPool pool;
  private final Logger logger;
  private final Map<Class<?>, ConfigSerializationHandler<?>> handlers;

  @Inject
  public DefaultConfigSerializationService(@InjectLogger Logger logger) {
    this.pool = ClassPool.getDefault();
    this.logger = logger;
    this.handlers = new HashMap<>();
  }

  @Override
  public boolean hasSerializer(Class<?> interfaceType) {
    return this.getSerializer(interfaceType) != null;
  }

  @Override
  public boolean hasSerializer(CtClass interfaceType) {
    for (Map.Entry<Class<?>, ConfigSerializationHandler<?>> entry : this.handlers.entrySet()) {
      try {
        if (interfaceType.subtypeOf(this.pool.get(entry.getKey().getName()))) {
          return true;
        }
      } catch (NotFoundException e) {
        this.logger.trace("Failed to load CtClass " + entry.getKey().getName() + " to check if a serializer for " +
            interfaceType.getName() + " exists", e);
      }
    }

    return false;
  }

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

  @Override
  public <T> void registerSerializer(Class<T> interfaceType, ConfigSerializationHandler<T> handler) {
    this.handlers.put(interfaceType, handler);
  }

  @Override
  public <T> JsonElement serialize(Class<T> interfaceType, T value) {
    ConfigSerializationHandler<T> handler = this.getSerializer(interfaceType);
    return handler != null ? handler.serialize(value) : null;
  }

  @Override
  public <T> T deserialize(Class<T> interfaceType, JsonElement value) {
    ConfigSerializationHandler<T> handler = this.getSerializer(interfaceType);
    return handler != null ? handler.deserialize(value) : null;
  }

  @Override
  public void discover(AnnotationMeta<ConfigSerializer> meta) {
    Class<?> interfaceType = meta.getAnnotation().value();
    CtClass serializerType = (CtClass) meta.getIdentifier().getLocation();

    this.handlers.put(interfaceType, InjectionHolder.getInjectedInstance(CtResolver.get(serializerType)));
  }
}
