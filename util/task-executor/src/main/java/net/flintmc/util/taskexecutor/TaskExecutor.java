package net.flintmc.util.taskexecutor;

import java.util.Set;
import java.util.function.Consumer;

public interface TaskExecutor {

  void schedule(Task task);

  void unSchedule(Task task);

  void run(Runnable runnable);

  void runAsync(Runnable runnable);

  Task scheduleSync(int ticks, Runnable runnable);

  Task scheduleSync(int ticks, Consumer<Task> runnable);

  Task scheduleAsync(int ticks, Runnable runnable);

  Task scheduleAsync(int ticks, Consumer<Task> runnable);

  Task scheduleSyncRepeating(int ticks, int interval, Runnable runnable);

  Task scheduleSyncRepeating(int ticks, int interval, Consumer<Task> runnable);

  Task scheduleAsyncRepeating(int ticks, int interval, Runnable runnable);

  Task scheduleAsyncRepeating(int ticks, int interval, Consumer<Task> runnable);

  Set<Task> getTasks();

  Set<Task> getSyncTasks();

  Set<Task> getAsyncTasks();

  Set<Task> getRepeatingTasks();

  Set<Task> getSyncRepeatingTasks();

  Set<Task> getAsyncRepeatingTasks();
}
