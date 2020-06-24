package net.labyfy.internal.component.tasks;

import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.subproperty.TaskBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler {

  @Inject
  private TaskService() {}

  public void discover(Identifier.Base property) {
    Task task = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    TaskExecutor taskExecutor = InjectionHolder.getInjectedInstance(task.executor());
    Collection<Property.Base> taskBodies = property.getProperty().getSubProperties(TaskBody.class);
    for (Property.Base taskBody : taskBodies) {
      taskExecutor.register(
          task,
          taskBody.getLocatedIdentifiedAnnotation().<TaskBody>getAnnotation().priority(),
          taskBody.getLocatedIdentifiedAnnotation().getLocation());
    }
  }
}
