package net.flintmc.framework.tasks.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import javassist.CtMethod;
import net.flintmc.framework.inject.InjectedInvocationHelper;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.TaskExecutionException;
import net.flintmc.framework.tasks.TaskExecutor;
import net.flintmc.framework.tasks.Tasks;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** This class is responsible for the execution of all registered tasks. */
@Singleton
@Implement(TaskExecutor.class)
public class DefaultTaskExecutor implements TaskExecutor {

  private final ConcurrentHashMap<Task, List<Pair<Double, CtMethod>>> methods;
  private final InjectedInvocationHelper injectedInvocationHelper;

  @Inject
  protected DefaultTaskExecutor(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.methods = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} */
  public final void register(Task task, CtMethod method) {
    List<Pair<Double, CtMethod>> bodies = methods.computeIfAbsent(task, t -> new ArrayList<>());
    bodies.add(Pair.of(task.priority(), method));
    bodies.sort(Map.Entry.comparingByKey());
  }

  /** {@inheritDoc} */
  public void execute(Tasks name) throws TaskExecutionException {
    this.execute(name, Maps.newConcurrentMap());
  }

  /** {@inheritDoc} */
  public void execute(Tasks name, Map<Key<?>, ?> arguments) throws TaskExecutionException {
    for (Map.Entry<Task, List<Pair<Double, CtMethod>>> entry : this.methods.entrySet()) {
      if (!entry.getKey().value().equals(name)) continue;
      for (int i = 0; i < entry.getValue().size(); i++) {
        CtMethod value = entry.getValue().get(i).getValue();

        try {
          this.injectedInvocationHelper.invokeMethod(CtResolver.get(value), arguments);
        } catch (InvocationTargetException exception) {
          throw new TaskExecutionException(
              value.getDeclaringClass().getName() + "#" + value.getName() + " threw an exception",
              exception);
        } catch (IllegalAccessException exception) {
          throw new TaskExecutionException(
              "unable to access method definition: "
                  + value.getDeclaringClass().getName()
                  + "#"
                  + value.getName(),
              exception);
        }

        while (i < entry.getValue().size() && !value.equals(entry.getValue().get(i).getValue()))
          i++;
      }
    }
  }
}
