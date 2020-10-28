package net.labyfy.internal.component.settings.options.service;

import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.Identifier;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.internal.component.settings.InvalidSettingAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Implement(SettingHandler.class)
@Service(value = RegisterSettingHandler.class, priority = -1 /* load before the SettingsDiscoverer */)
public class SettingHandlerService implements SettingHandler<Annotation>, ServiceHandler<RegisterSettingHandler> {

  private final Map<Class<? extends Annotation>, SettingHandler> handlers;
  private final Map<Class<? extends Annotation>, CtClass> pendingHandlers;

  public SettingHandlerService() {
    this.handlers = new HashMap<>();
    this.pendingHandlers = new ConcurrentHashMap<>();
  }

  @Override
  public Object getDefaultValue(Annotation annotation, ConfigObjectReference reference) {
    this.processPendingHandlers();

    SettingHandler handler = this.handlers.get(annotation.annotationType());
    if (handler == null) {
      try {
        return annotation.annotationType().getDeclaredMethod("defaultValue").invoke(annotation);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new InvalidSettingAnnotationException("Failed to read defaultValue from annotation "
            + annotation.annotationType().getName() + ", invalid/missing defaultValue() method", e);
      }
    }

    return handler.getDefaultValue(annotation, reference);
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, Annotation annotation) {
    this.processPendingHandlers();

    SettingHandler handler = this.handlers.get(annotation.annotationType());

    return handler == null || handler.isValidInput(input, reference, annotation);
  }

  private void processPendingHandlers() {
    if (this.pendingHandlers.isEmpty()) {
      return;
    }

    this.pendingHandlers.forEach((annotationType, type) ->
        this.handlers.put(annotationType, InjectionHolder.getInjectedInstance(CtResolver.get(type)))
    );

    this.pendingHandlers.clear();
  }

  @Override
  public void discover(AnnotationMeta<RegisterSettingHandler> meta) throws ServiceNotFoundException {
    Class<? extends Annotation> annotationType = meta.getAnnotation().value();
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass type = identifier.getLocation();

    if (!annotationType.isAnnotationPresent(ApplicableSetting.class)) {
      throw new ServiceNotFoundException("Invalid SettingHandler registered for handler " + type.getName()
          + ": The annotation " + annotationType.getName() + " is not annotated with " + ApplicableSetting.class.getName());
    }

    if (this.pendingHandlers.containsKey(annotationType)) {
      throw new ServiceNotFoundException("Failed to register SettingHandler " + type.getName()
          + ": Cannot register multiple handlers for the annotation " + annotationType.getName());
    }

    this.pendingHandlers.put(annotationType, type);
  }
}
