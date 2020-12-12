package net.flintmc.transform.hook.internal;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.transform.hook.Hook.ExecutionTime;

import java.util.Map;

@Singleton
public class HookServiceNotifier {

  private static final Key<ExecutionTime> EXECUTION_TIME_KEY = Key.get(ExecutionTime.class);
  private static final Key<Object> INSTANCE_KEY = Key.get(Object.class, Names.named("instance"));
  private static final Key<Object[]> ARGS_KEY = Key.get(Object[].class, Names.named("args"));

  public Object notify(
      Object instance,
      ExecutionTime executionTime,
      MethodInjector injector,
      Object[] args) {
    Map<Key<?>, Object> availableParameters = Maps.newHashMap();
    availableParameters.put(EXECUTION_TIME_KEY, executionTime);

    if (instance != null) { // if the instance is null, the hooked method is a static method
      availableParameters.put(INSTANCE_KEY, instance);
    }

    availableParameters.put(ARGS_KEY, args);

    return injector.invoke(availableParameters);
  }
}
