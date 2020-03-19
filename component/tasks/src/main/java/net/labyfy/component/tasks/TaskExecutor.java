package net.labyfy.component.tasks;

import com.google.common.collect.*;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import net.labyfy.component.inject.InjectionHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
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

  @Inject
  protected TaskExecutor() {
    InjectionHolder.getInstance()
        .addInitializationListener(() -> execute(Tasks.PRE_MINECRAFT_INITIALIZE));
    this.methods =
        Multimaps.newListMultimap(
            new TreeMap<>((o1, o2) -> StringUtils.compare(o1.value(), o2.value())),
            () -> SortedList_1x0.of(Lists.newArrayList(), Comparator.comparing(Pair::getKey)));
  }

  public final void register(Task task, double priority, Method method) {
    methods.put(task, Pair.of(priority, method));
  }

  public void execute(String name) {
    for (Map.Entry<Task, Pair<Double, Method>> entry : this.methods.entries()) {
      if (!entry.getKey().value().equals(name)) continue;
      Method value = entry.getValue().getValue();
      this.call(
          InjectionHolder.getInjectedInstance(value.getDeclaringClass()),
          value);
    }
  }

  private void call(Object object, Method method) {
    Map<? extends Class<?>, ?> dependencies =
        InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
            .getDependencies().stream()
            .map(Dependency::getKey)
            .map(Key::getTypeLiteral)
            .map(TypeLiteral::getRawType)
            .collect(
                Collectors.toMap(
                    type -> type,
                    type -> InjectionHolder.getInjectedInstance(type)));

    Object[] arguments = new Object[method.getParameterTypes().length];
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      arguments[i] = dependencies.get(parameterTypes[i]);
    }
    method.setAccessible(true);
    try {
      method.invoke(object, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
