package net.flintmc.util.taskexecutor.internal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.taskexecutor.Task;
import net.flintmc.util.taskexecutor.TaskExecutor;

import java.util.function.Consumer;

@Implement(Task.class)
public class InternalTask implements Task {

  private int ticks;
  private int interval;

  private boolean scheduled;

  private final boolean async;
  private final boolean repeat;
  private final Consumer<Task> runnable;
  private final TaskExecutor taskExecutor;

  @AssistedInject
  private InternalTask(
      @Assisted("ticks") int ticks,
      @Assisted("interval") int interval,
      @Assisted("async") boolean async,
      @Assisted("repeat") boolean repeat,
      @Assisted("runnable") Consumer<Task> runnable,
      TaskExecutor taskExecutor) {
    this.ticks = ticks;
    this.interval = interval;
    this.async = async;
    this.repeat = repeat;
    this.runnable = runnable;
    this.taskExecutor = taskExecutor;
    this.scheduled = false;
  }

  @Override
  public void schedule() {
    this.scheduled = true;
    this.taskExecutor.schedule(this);
  }

  @Override
  public void run() {
    this.runnable.accept(this);
  }

  @Override
  public void cancel() {
    this.scheduled = false;
    this.taskExecutor.unschedule(this);
  }

  @Override
  public boolean isAsync() {
    return this.async;
  }

  @Override
  public boolean isRepeating() {
    return this.repeat;
  }

  @Override
  public boolean isScheduled() {
    return this.scheduled;
  }

  @Override
  public int getTicksToStart() {
    return this.ticks;
  }

  @Override
  public int getInterval() {
    return this.interval;
  }

  @Override
  public void setTicksToStart(int ticks) {
    this.ticks = ticks;
  }

  @Override
  public void setInterval(int interval) {
    this.interval = interval;
  }
}
