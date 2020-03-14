package net.labyfy.component.tasks;

import com.google.inject.Injector;
import net.labyfy.component.tasks.property.TaskBody;
import net.labyfy.component.tasks.property.TaskBodyPriority;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler {

  private final Injector injector;

  @Inject
  private TaskService(Injector injector) {
    this.injector = injector;
  }

  public void discover(Identifier.Base property) {
    Task task = (Task) property.getLocatedIdentifiedAnnotation().getAnnotation();
    TaskExecutor taskExecutor = this.injector.getInstance(task.executor());
    Collection<Property.Base> taskBodies = property.getProperty().getSubProperties(TaskBody.class);
    for (Property.Base taskBody : taskBodies) {
      TaskBodyPriority taskPriority =
          (TaskBodyPriority)
              taskBody.getSubProperties(TaskBodyPriority.class).stream()
                  .map(Property.Base::getLocatedIdentifiedAnnotation)
                  .map(LocatedIdentifiedAnnotation::getAnnotation)
                  .findAny()
                  .orElse(null);
      taskExecutor.register(
          task,
          taskPriority == null ? 0 : taskPriority.value(),
          taskBody.getLocatedIdentifiedAnnotation().getLocation());
    }
  }
}
