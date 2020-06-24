package net.labyfy.component.tasks;

import com.google.common.collect.*;
import com.google.inject.Key;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TaskExecutor {

  private final ConcurrentHashMap<Task, List<Pair<Double, Method>>> methods;
  private final InjectedInvocationHelper injectedInvocationHelper;

  @Inject
  protected TaskExecutor(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    InjectionHolder.getInstance()
        .addInitializationListener(() -> execute(Tasks.PRE_MINECRAFT_INITIALIZE));
    this.methods = new ConcurrentHashMap<>();
  }

  public final void register(Task task, double priority, Method method) {
    method.setAccessible(true);
    List<Pair<Double, Method>> bodies = methods.computeIfAbsent(task, t -> new ArrayList<>());
    bodies.add(Pair.of(priority, method));
    bodies.sort(Map.Entry.comparingByKey());
  }

  public void execute(String name) {
    this.execute(name, Maps.newConcurrentMap());
  }

  public void execute(String name, Map<Key<?>, ?> arguments) {
    for (Map.Entry<Task, List<Pair<Double, Method>>> entry : this.methods.entrySet()) {
      if (!entry.getKey().value().equals(name)) continue;
      for (int i = 0; i < entry.getValue().size(); i++) {
        Method value = entry.getValue().get(i).getValue();
        this.injectedInvocationHelper.invokeMethod(value, arguments);
        while (i < entry.getValue().size() && !value.equals(entry.getValue().get(i).getValue()))
          i++;
      }
    }
  }
}
