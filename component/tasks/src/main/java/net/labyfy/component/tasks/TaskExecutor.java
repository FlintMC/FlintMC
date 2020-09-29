package net.labyfy.component.tasks;

import com.google.inject.Key;

import java.lang.reflect.Method;
import java.util.Map;

public interface TaskExecutor {
  void register(Task task, Method method);

  void execute(Tasks name) throws TaskExecutionException;

  void execute(Tasks name, Map<Key<?>, ?> arguments) throws TaskExecutionException;
}
