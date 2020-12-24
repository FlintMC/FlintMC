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

import java.util.Set;
import java.util.function.Consumer;

/**
 * Utility to schedule and execute {@link Task}s, async or sync, repeating or not.
 */
public interface TaskExecutor {

  /**
   * Schedules the given {@link Task}.
   *
   * @param task the task to schedule.
   */
  void schedule(Task task);

  /**
   * Removes a given {@link Task} from the execution schedule.
   *
   * @param task the task to un-schedule
   */
  void unSchedule(Task task);

  /**
   * Creates and executes a task synchronous to Minecraft.
   *
   * @param runnable the {@link Runnable} to execute
   */
  void run(Runnable runnable);

  /**
   * Creates and executes a task asynchronous to Minecraft
   *
   * @param runnable the {@link Runnable} to execute
   */
  void runAsync(Runnable runnable);

  /**
   * Schedules a synchronous task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param runnable the {@link Runnable} to execute
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleSync(int ticks, Runnable runnable);

  /**
   * Schedules a synchronous task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param runnable the {@link Consumer} to execute. Will receive the {@link Task} instance as
   *                 argument.
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleSync(int ticks, Consumer<Task> runnable);

  /**
   * Schedules an asynchronous task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param runnable the {@link Runnable} to execute
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleAsync(int ticks, Runnable runnable);

  /**
   * Schedules an asynchronous task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param runnable the {@link Consumer} to execute. Will receive the {@link Task} instance as
   *                 argument.
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleAsync(int ticks, Consumer<Task> runnable);

  /**
   * Schedules a synchronous repeating task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param interval the interval between executions in ticks
   * @param runnable the {@link Runnable} to execute
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleSyncRepeating(int ticks, int interval, Runnable runnable);

  /**
   * Schedules a synchronous repeating task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param interval the interval between executions in ticks
   * @param runnable the {@link Consumer} to execute. Will receive the {@link Task} instance as
   *                 argument.
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleSyncRepeating(int ticks, int interval, Consumer<Task> runnable);

  /**
   * Schedules an asynchronous repeating task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param interval the interval between executions in ticks
   * @param runnable the {@link Runnable} to execute
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleAsyncRepeating(int ticks, int interval, Runnable runnable);

  /**
   * Schedules an asynchronous repeating task.
   *
   * @param ticks    amount of ticks that should pass before execution
   * @param interval the interval between executions in ticks
   * @param runnable the {@link Consumer} to execute. Will receive the {@link Task} instance as
   *                 argument.
   * @return the newly created and already scheduled {@link Task}
   */
  Task scheduleAsyncRepeating(int ticks, int interval, Consumer<Task> runnable);

  /**
   * @return all currently scheduled {@link Task}s
   */
  Set<Task> getTasks();

  /**
   * @return all currently scheduled synchronous {@link Task}s
   */
  Set<Task> getSyncTasks();

  /**
   * @return all currently scheduled asynchronous {@link Task}s
   */
  Set<Task> getAsyncTasks();

  /**
   * @return all currently scheduled repeating {@link Task}s
   */
  Set<Task> getRepeatingTasks();

  /**
   * @return all currently scheduled synchronous repeating {@link Task}s
   */
  Set<Task> getSyncRepeatingTasks();

  /**
   * @return all currently scheduled asynchronous repeating {@link Task}s
   */
  Set<Task> getAsyncRepeatingTasks();
}
