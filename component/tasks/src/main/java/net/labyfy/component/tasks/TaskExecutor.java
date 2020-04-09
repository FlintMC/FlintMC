package net.labyfy.component.tasks;

import com.google.common.collect.*;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.happy.collections.lists.decorators.SortedList_1x0;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class TaskExecutor {

  private final Multimap<Task, Pair<Double, Method>> methods;
  private final InjectedInvocationHelper injectedInvocationHelper;

  @Inject
  protected TaskExecutor(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    InjectionHolder.getInstance()
        .addInitializationListener(() -> execute(Tasks.PRE_MINECRAFT_INITIALIZE));
    this.methods =
        Multimaps.newListMultimap(
            new TreeMap<>((o1, o2) -> StringUtils.compare(o1.value(), o2.value())),
            () -> SortedList_1x0.of(Lists.newArrayList(), Comparator.comparing(Pair::getKey)));
  }

  public final void register(Task task, double priority, Method method) {
    method.setAccessible(true);
    methods.put(task, Pair.of(priority, method));
  }

  public void execute(String name) {
    this.execute(name, Maps.newConcurrentMap());
  }

  public void execute(String name, Map<Key<?>, ?> arguments) {
    for (Map.Entry<Task, Pair<Double, Method>> entry : this.methods.entries()) {
      if (!entry.getKey().value().equals(name)) continue;
      Method value = entry.getValue().getValue();
      this.injectedInvocationHelper.invokeMethod(value, arguments);
    }
  }

  private void call(Object object, Method method) {}
}
