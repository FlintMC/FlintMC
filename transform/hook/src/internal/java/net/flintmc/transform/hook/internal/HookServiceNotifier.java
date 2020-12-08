package net.flintmc.transform.hook.internal;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.flintmc.framework.inject.OptimizedMethodInjector;
import net.flintmc.transform.hook.Hook.ExecutionTime;

import java.util.Map;

@Singleton
public class HookServiceNotifier {

  public Object notify(
      Object instance,
      ExecutionTime executionTime,
      OptimizedMethodInjector injector,
      Object[] args) {
    Map<Key<?>, Object> availableParameters = Maps.newHashMap();
    availableParameters.put(Key.get(ExecutionTime.class), executionTime);

    if (instance != null) { // if the instance is null, the hooked method is a static method
      availableParameters.put(Key.get(Object.class, Names.named("instance")), instance);
      availableParameters.put(Key.get(instance.getClass()), instance);
    }

    availableParameters.put(Key.get(Object[].class, Names.named("args")), args);

    return injector.invoke(availableParameters);
  }
}
