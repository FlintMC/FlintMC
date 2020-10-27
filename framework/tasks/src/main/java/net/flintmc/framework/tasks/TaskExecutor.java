package net.flintmc.framework.tasks;

import com.google.inject.Key;
import javassist.CtMethod;

import java.util.Map;

public interface TaskExecutor {
  void register(Task task, CtMethod method);

  void execute(Tasks name) throws TaskExecutionException;

  void execute(Tasks name, Map<Key<?>, ?> arguments) throws TaskExecutionException;
}
