package net.flintmc.util.taskexecutor;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.function.Consumer;

public interface Task extends Runnable {

  void schedule();

  void run();

  void cancel();

  boolean isAsync();

  boolean isRepeating();

  boolean isScheduled();

  int getTicksToStart();

  int getInterval();

  void setTicksToStart(int ticks);

  void setInterval(int interval);

  @AssistedFactory(Task.class)
  interface Factory {

    Task create(
        @Assisted("ticks") int ticks,
        @Assisted("interval") int interval,
        @Assisted("async") boolean async,
        @Assisted("repeat") boolean repeat,
        @Assisted("runnable") Consumer<Task> runnable);
  }
}
