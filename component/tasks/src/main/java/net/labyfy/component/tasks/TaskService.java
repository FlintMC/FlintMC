package net.labyfy.component.tasks;

import com.google.inject.Injector;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.tasks.subproperty.TaskBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Service(Task.class)
@Singleton
public class TaskService implements ServiceHandler {

  @Inject private Injector injector;

  @Inject
  private TaskService() {}

  public void discover(Identifier.Base property) {
    Task task = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    TaskExecutor taskExecutor = injector.getInstance(task.executor());
    Collection<Property.Base> taskBodies = property.getProperty().getSubProperties(TaskBody.class);
    for (Property.Base taskBody : taskBodies) {
      taskExecutor.register(
          task,
          taskBody.getLocatedIdentifiedAnnotation().<TaskBody>getAnnotation().priority(),
          taskBody.getLocatedIdentifiedAnnotation().getLocation());
    }
  }
}
