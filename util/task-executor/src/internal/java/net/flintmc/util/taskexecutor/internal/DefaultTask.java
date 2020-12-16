package net.flintmc.util.taskexecutor.internal;

import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.taskexecutor.Task;
import net.flintmc.util.taskexecutor.TaskExecutor;

/**
 * {@inheritDoc}
 */
@Implement(Task.class)
public class DefaultTask implements Task {

  private int ticks;
  private int interval;

  private boolean scheduled;

  private final boolean async;
  private final boolean repeat;
  private final Consumer<Task> runnable;
  private final TaskExecutor taskExecutor;

  @AssistedInject
  private DefaultTask(
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void schedule() {
    this.scheduled = true;
    this.taskExecutor.schedule(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    this.runnable.accept(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancel() {
    this.scheduled = false;
    this.taskExecutor.unSchedule(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAsync() {
    return this.async;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRepeating() {
    return this.repeat;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isScheduled() {
    return this.scheduled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTicksToStart() {
    return this.ticks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getInterval() {
    return this.interval;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTicksToStart(int ticks) {
    this.ticks = ticks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInterval(int interval) {
    this.interval = interval;
  }
}
