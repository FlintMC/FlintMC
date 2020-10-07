package net.labyfy.internal.component.tasks;

import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.TaskExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

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
  public void discover(IdentifierMeta<Task> identifierMeta) throws ServiceNotFoundException {
    Task annotation = identifierMeta.getAnnotation();
    TaskExecutor executor = InjectionHolder.getInjectedInstance(annotation.executor());
    executor.register(annotation, identifierMeta.getTarget());
  }

}
