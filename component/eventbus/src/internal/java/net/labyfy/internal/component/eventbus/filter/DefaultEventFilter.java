package net.labyfy.internal.component.eventbus.filter;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.event.filter.EventFilter;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.internal.component.stereotype.annotation.AnnotationCollector;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link EventFilter}.
 */
@Implement(EventFilter.class)
public class DefaultEventFilter implements EventFilter {

  private final Map<Class<?>, EventFilterMapping[]> mappings = new HashMap<>();

  private final Logger logger;

  @Inject
  public DefaultEventFilter(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  @Override
  public boolean matches(Object event, SubscribeMethod method) {
    if (method.getExtraAnnotation() == null) {
      return true;
    }

    if (!this.mappings.containsKey(event.getClass())) {
      this.mappings.put(event.getClass(), this.createMappings(event.getClass(), method.getExtraAnnotation()));
    }

    EventFilterMapping[] mappings = this.mappings.get(event.getClass());

    try {
      for (EventFilterMapping mapping : mappings) {
        if (!mapping.matches(event, method.getExtraAnnotation())) {
          return false;
        }
      }
      return true;
    } catch (InvocationTargetException | IllegalAccessException e) {
      this.logger.error("Error while trying to match the event " + event.getClass().getName() + " with the annotation " + method.getExtraAnnotation().getClass().getName());
      return false;
    }
  }

  private EventFilterMapping[] createMappings(Class<?> eventClass, Annotation annotation) {
    Class<? extends Annotation> annotationClass = AnnotationCollector.getRealAnnotationClass(annotation);

    Collection<EventFilterMapping> mappings = new ArrayList<>();

    for (Method annotationMethod : annotationClass.getDeclaredMethods()) {

      Named named = annotationMethod.getAnnotation(Named.class);

      for (Method eventMethod : eventClass.getMethods()) {
        if (eventMethod.getParameterCount() != 0) {
          continue;
        }

        if (named != null && eventMethod.isAnnotationPresent(Named.class)) {
          if (named.value().equals(eventMethod.getAnnotation(Named.class).value())) {
            mappings.add(new EventFilterMapping(annotationMethod, eventMethod));
            break;
          }

          continue;
        }

        if (eventMethod.getReturnType().equals(annotationMethod.getReturnType())) {
          mappings.add(new EventFilterMapping(annotationMethod, eventMethod));
          break;
        }
      }

    }

    return mappings.toArray(new EventFilterMapping[0]);
  }

}
