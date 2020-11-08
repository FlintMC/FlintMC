package net.flintmc.framework.config.internal.serialization;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
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
