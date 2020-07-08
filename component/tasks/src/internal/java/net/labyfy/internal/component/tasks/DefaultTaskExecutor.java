package net.labyfy.internal.component.tasks;


import com.google.common.collect.Maps;
import com.google.inject.Key;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.TaskExecutionException;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.Tasks;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is responsible for the execution of all registered tasks.
 */
@Singleton
@Implement(TaskExecutor.class)
public class DefaultTaskExecutor implements TaskExecutor {

  private final ConcurrentHashMap<Task, List<Pair<Double, Method>>> methods;
  private final InjectedInvocationHelper injectedInvocationHelper;

  @Inject
  protected DefaultTaskExecutor(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    InjectionHolder.getInstance()
        .addInitializationListener(() -> execute(Tasks.PRE_MINECRAFT_INITIALIZE));
    this.methods = new ConcurrentHashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  public final void register(Task task, Method method) {
    method.setAccessible(true);
    List<Pair<Double, Method>> bodies = methods.computeIfAbsent(task, t -> new ArrayList<>());
    bodies.add(Pair.of(task.priority(), method));
    bodies.sort(Map.Entry.comparingByKey());
  }

  /**
   * {@inheritDoc}
   */
  public void execute(String name) throws TaskExecutionException {
    this.execute(name, Maps.newConcurrentMap());
  }

  /**
   * {@inheritDoc}
   */
  public void execute(String name, Map<Key<?>, ?> arguments) throws TaskExecutionException {
    for (Map.Entry<Task, List<Pair<Double, Method>>> entry : this.methods.entrySet()) {
      if (!entry.getKey().value().equals(name)) continue;
      for (int i = 0; i < entry.getValue().size(); i++) {
        Method value = entry.getValue().get(i).getValue();

        try {
          this.injectedInvocationHelper.invokeMethod(value, arguments);
        } catch (InvocationTargetException exception) {
          throw new TaskExecutionException(value.getDeclaringClass().getName() + "#" + value.getName() + " threw an exception", exception);
        } catch (IllegalAccessException exception) {
          throw new TaskExecutionException("unable to access method definition: " + value.getDeclaringClass().getName() + "#" + value.getName(), exception);
        }

        while (i < entry.getValue().size() && !value.equals(entry.getValue().get(i).getValue()))
          i++;
      }
    }
  }
}
