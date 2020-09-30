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
    if (method.getGroupAnnotations().isEmpty()) {
      return true;
    }

    try {

      if (!this.mappings.containsKey(event.getClass())) {
        this.mappings.put(event.getClass(), this.createMappings(method.getEventMethod().getParameterTypes()[0], method.getGroupAnnotations()));
      }

      EventFilterMapping[] mappings = this.mappings.get(event.getClass());

      for (EventFilterMapping mapping : mappings) {
        for (Annotation annotation : method.getGroupAnnotations()) {
          if (!mapping.canMatch(annotation)) {
            continue;
          }
          if (!mapping.matches(event, annotation)) {
            return false;
          }
        }
      }
      return true;
    } catch (InvocationTargetException | IllegalAccessException e) {
      this.logger.error("Error while trying to match the event " + event.getClass().getName() + " with the annotation " + method.getGroupAnnotations().getClass().getName());
      return false;
    }
  }

  private EventFilterMapping[] createMappings(Class<?> eventClass, Collection<Annotation> annotations) {
    Collection<EventFilterMapping> mappings = new ArrayList<>();

    for (Annotation annotation : annotations) {
      Class<? extends Annotation> annotationClass = AnnotationCollector.getRealAnnotationClass(annotation);
      this.addMappings(eventClass, annotationClass, mappings);
    }

    return mappings.toArray(new EventFilterMapping[0]);
  }

  private void addMappings(Class<?> eventClass, Class<? extends Annotation> annotationClass, Collection<EventFilterMapping> mappings) {
    for (Method annotationMethod : annotationClass.getDeclaredMethods()) {

      Named named = annotationMethod.getAnnotation(Named.class);

      for (Method eventMethod : eventClass.getMethods()) {
        if (eventMethod.getParameterCount() != 0 || (named != null && !eventMethod.isAnnotationPresent(Named.class))) {
          continue;
        }

        if ((named != null && named.value().equals(eventMethod.getAnnotation(Named.class).value())) ||
            eventMethod.getReturnType().equals(annotationMethod.getReturnType())) {
          mappings.add(new EventFilterMapping(annotationMethod, eventMethod));
          break;
        }

      }
    }
  }

}
