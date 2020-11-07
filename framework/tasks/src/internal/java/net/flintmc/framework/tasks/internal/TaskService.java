package net.flintmc.framework.tasks.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.TaskExecutor;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.MethodIdentifier;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler<Task> {

  @Inject
  private TaskService() {}

  /** {@inheritDoc} */
  @Override
  public void discover(AnnotationMeta<Task> identifierMeta) throws ServiceNotFoundException {
    Task annotation = identifierMeta.getAnnotation();
    TaskExecutor executor = InjectionHolder.getInjectedInstance(annotation.executor());
    executor.register(annotation, identifierMeta.<MethodIdentifier>getIdentifier().getLocation());
  }
}
