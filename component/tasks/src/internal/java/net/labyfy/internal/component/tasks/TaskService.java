package net.labyfy.internal.component.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.MethodIdentifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.TaskExecutor;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler<Task> {

  @Inject
  private TaskService() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<Task> identifierMeta) throws ServiceNotFoundException {
    Task annotation = identifierMeta.getAnnotation();
    TaskExecutor executor = InjectionHolder.getInjectedInstance(annotation.executor());
    executor.register(annotation, identifierMeta.<MethodIdentifier>getIdentifier().getLocation());
  }

}
