package net.labyfy.internal.component.tasks;

import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.TaskExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler {

  @Inject
  private TaskService() {
  }

  /**
   * {@inheritDoc}
   */
  public void discover(Identifier.Base property) {
    Task task = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    TaskExecutor taskExecutor = InjectionHolder.getInjectedInstance(task.executor());
    taskExecutor.register(
        task, property.getProperty().getLocatedIdentifiedAnnotation().getLocation());
  }
}
