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

package net.flintmc.util.taskexecutor.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.TickEvent;
import net.flintmc.util.taskexecutor.Task;
import net.flintmc.util.taskexecutor.TaskExecutor;

/**
 * {@inheritDoc}
 */
@Singleton
@Implement(TaskExecutor.class)
public class DefaultTaskExecutor implements TaskExecutor {

  private final Task.Factory taskFactory;
  private final ExecutorService executorService;

  private final Set<Task> scheduledTasks;

  @Inject
  private DefaultTaskExecutor(Task.Factory taskFactory, ExecutorService executorService) {
    this.taskFactory = taskFactory;
    this.executorService = executorService;
    this.scheduledTasks = new HashSet<>();
  }

  @PostSubscribe
  public void onTick(TickEvent event) {
    if (event.getType() != TickEvent.Type.GENERAL) {
      return;
    }

    Set<Task> tasks;
    synchronized (this) {
      tasks = new HashSet<>(this.scheduledTasks);
    }
    for (Task task : tasks) {
      if (task.getTicksToStart() == 0) {
        if (task.isRepeating()) {
          task.setTicksToStart(task.getInterval());
        } else {
          task.cancel();
        }
        if (task.isAsync()) {
          this.executorService.submit(task);
        } else {
          task.run();
        }
      }
      task.setTicksToStart(task.getTicksToStart() - 1);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void schedule(Task task) {
    if (task.isAsync() && task.getTicksToStart() == 0 && !task.isRepeating()) {
      this.executorService.submit(task);
    } else {
      synchronized (this) {
        this.scheduledTasks.add(task);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unSchedule(Task task) {
    synchronized (this) {
      this.scheduledTasks.remove(task);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(Runnable runnable) {
    this.taskFactory.create(0, 0, false, false, t -> runnable.run()).schedule();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void runAsync(Runnable runnable) {
    this.taskFactory.create(0, 0, true, false, t -> runnable.run()).schedule();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleSync(int ticks, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, 0, false, false, t -> runnable.run());
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleSync(int ticks, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, 0, false, false, runnable);
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleAsync(int ticks, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, 0, true, false, t -> runnable.run());
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleAsync(int ticks, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, 0, true, false, runnable);
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleSyncRepeating(int ticks, int interval, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, interval, false, true, t -> runnable.run());
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleSyncRepeating(int ticks, int interval, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, interval, false, true, runnable);
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleAsyncRepeating(int ticks, int interval, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, interval, true, true, t -> runnable.run());
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Task scheduleAsyncRepeating(int ticks, int interval, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, interval, true, true, runnable);
    task.schedule();
    return task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getTasks() {
    return this.scheduledTasks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getSyncTasks() {
    return this.scheduledTasks.stream().filter(t -> !t.isAsync()).collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getAsyncTasks() {
    return this.scheduledTasks.stream().filter(Task::isAsync).collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getRepeatingTasks() {
    return this.scheduledTasks.stream().filter(Task::isRepeating).collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getSyncRepeatingTasks() {
    return this.scheduledTasks.stream()
        .filter(t -> !t.isAsync() && t.isRepeating())
        .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Task> getAsyncRepeatingTasks() {
    return this.scheduledTasks.stream()
        .filter(t -> t.isAsync() && t.isRepeating())
        .collect(Collectors.toSet());
  }
}
