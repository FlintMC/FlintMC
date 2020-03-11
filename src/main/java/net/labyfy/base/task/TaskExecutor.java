package net.labyfy.base.task;

import com.google.common.collect.*;
import com.google.inject.Injector;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import org.happy.collections.lists.decorators.SortedList_1x0;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class TaskExecutor {

  private final Multimap<String, Pair<Double, Method>> methods;

  @Inject private Injector injector;

  @Inject
  protected TaskExecutor() {
    this.methods =
        Multimaps.newListMultimap(
            new TreeMap<>(),
            () -> SortedList_1x0.of(Lists.newArrayList(), Comparator.comparing(Pair::getKey)));
  }

  public final void register(String name, double priority, Method method) {
    methods.put(name, Pair.of(priority, method));
  }

  public void execute(String name) {
    for (Map.Entry<Double, Method> entry : this.methods.get(name)) {
      try {
        entry.getValue().invoke(this.injector.getInstance(entry.getValue().getDeclaringClass()));
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
