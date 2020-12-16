package net.flintmc.util.taskexecutor;

import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents a task that can be scheduled and executed (sync or async).
 */
public interface Task extends Runnable {

  /**
   * Schedules this task for execution.
   */
  void schedule();

  /**
   * Executes this task.
   */
  void run();

  /**
   * Removes this task from the execution schedule if it is scheduled. Can be used to cancel
   * repeating tasks.
   */
  void cancel();

  /**
   * @return true, if this task shall be executed async to Minecraft's game loop/render thread,
   * false if not
   */
  boolean isAsync();

  /**
   * @return true, if this task should execute repeatedly, false if not
   */
  boolean isRepeating();

  /**
   * @return true if this task is currently scheduled, false if not
   */
  boolean isScheduled();

  /**
   * @return the current ticks that need to pass until this task shall execute
   */
  int getTicksToStart();

  /**
   * @return the the amount of ticks that should pass between executions if this task is a repeating
   * task
   */
  int getInterval();

  /**
   * Sets the ticks that should pass till this task should execute.
   *
   * @param ticks amount of ticks
   */
  void setTicksToStart(int ticks);

  /**
   * Sets the interval length between executions if this is a repeating task.
   *
   * @param interval interval length in ticks
   */
  void setInterval(int interval);

  @AssistedFactory(Task.class)
  interface Factory {

    /**
     * Creates a new task, but does not automatically schedule it.
     *
     * @param ticks    amount of ticks that should pass before the task executes
     * @param interval interval in ticks between executions if repeat is true
     * @param async    whether this task should execute async to Minecraft's game loop/render
     *                 thread
     * @param repeat   whether this task should be executed repeatedly
     * @param runnable the runnable that will be called if the task is executed. The {@link Task}
     *                 parameter will be the Task that owns the Consumer and can be used to cancel a
     *                 repeating task (e.g.).
     * @return the new {@link Task}
     */
    Task create(
        @Assisted("ticks") int ticks,
        @Assisted("interval") int interval,
        @Assisted("async") boolean async,
        @Assisted("repeat") boolean repeat,
        @Assisted("runnable") Consumer<Task> runnable);
  }
}
