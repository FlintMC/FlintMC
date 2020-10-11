package net.labyfy.internal.component.eventbus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.PostSubscribe;
import net.labyfy.component.eventbus.event.subscribe.PreSubscribe;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.method.Executor;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.internal.component.eventbus.exception.ExecutorGenerationException;

import java.lang.annotation.Annotation;

/** Service for sending events to receivers. */
@Singleton
@Service(
    value = {Subscribe.class, PreSubscribe.class, PostSubscribe.class},
    priority = -10000)
public class EventBusService implements ServiceHandler<Annotation> {

  private final Executor.Factory factory;
  private final SubscribeMethod.Factory subscribedMethodFactory;

  private final EventBus eventBus;

  @Inject
  private EventBusService(
      Executor.Factory executorFactory,
      SubscribeMethod.Factory subscribedMethodFactory,
      EventBus eventBus) {
    this.subscribedMethodFactory = subscribedMethodFactory;
    this.eventBus = eventBus;
    this.factory = executorFactory;
  }

  /** {@inheritDoc} */
  @Override
  public void discover(DetectableAnnotationProvider.AnnotationMeta<Annotation> annotationMeta)
      throws ServiceNotFoundException {

    Annotation subscribe = annotationMeta.getAnnotation();
    DetectableAnnotationProvider.AnnotationMeta.MethodIdentifier identifier =
        annotationMeta.getIdentifier();
    CtMethod method = identifier.getLocation();

    if (identifier.getParameters().length != 1) {
      throw new IllegalArgumentException(
          "Method "
              + method.getName()
              + " in "
              + method.getDeclaringClass().getName()
              + " doesn't have exactly one parameter");
    }

    CtClass eventClass;
    try {
      eventClass = method.getParameterTypes()[0];
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException("Failed to retrieve CtClass of parameter type.", e);
    }

    Executor executor;

    try {
      executor = this.factory.create(method.getDeclaringClass(), method);
    } catch (Throwable throwable) {
      throw new ExecutorGenerationException(
          "Encountered an exception while creating an event subscriber for method \""
              + method
              + "\"!",
          throwable);
    }

    byte priority;
    Subscribe.Phase phase;
    if (subscribe instanceof PreSubscribe) {
      priority = ((PreSubscribe) subscribe).priority();
      phase = Subscribe.Phase.PRE;
    } else if (subscribe instanceof PostSubscribe) {
      priority = ((PostSubscribe) subscribe).priority();
      phase = Subscribe.Phase.POST;
    } else if (subscribe instanceof Subscribe) {
      priority = ((Subscribe) subscribe).priority();
      phase = ((Subscribe) subscribe).phase();
    } else {
      throw new ExecutorGenerationException(
          "Unknown subscribe annotation: " + subscribe.annotationType().getName());
    }

    // Initializes a new subscribe method
    SubscribeMethod subscribeMethod =
        this.subscribedMethodFactory.create(
            priority, phase, method.getDeclaringClass(), executor, method);

    this.eventBus.getSubscribeMethods().put(eventClass.getName(), subscribeMethod);
  }
}
