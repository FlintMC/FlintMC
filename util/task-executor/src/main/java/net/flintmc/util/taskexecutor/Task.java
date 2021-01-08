/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
   * Sets the ticks that should pass till this task should execute.
   *
   * @param ticks amount of ticks
   */
  void setTicksToStart(int ticks);

  /**
   * @return the the amount of ticks that should pass between executions if this task is a repeating
   * task
   */
  int getInterval();

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
