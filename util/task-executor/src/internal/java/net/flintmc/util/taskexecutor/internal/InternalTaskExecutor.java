package net.flintmc.util.taskexecutor.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.taskexecutor.Task;
import net.flintmc.util.taskexecutor.TaskExecutor;
import net.flintmc.util.taskexecutor.TickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Singleton
@Implement(TaskExecutor.class)
public class InternalTaskExecutor implements TaskExecutor {

  private final Task.Factory taskFactory;
  private final ExecutorService executorService;

  private final Set<Task> scheduledTasks;

  @Inject
  private InternalTaskExecutor(Task.Factory taskFactory, ExecutorService executorService) {
    this.taskFactory = taskFactory;
    this.executorService = executorService;
    this.scheduledTasks = new HashSet<>();
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void onTick(TickEvent event) {
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
        if (task.isAsync()) this.executorService.submit(task);
        else task.run();
      }
      task.setTicksToStart(task.getTicksToStart() - 1);
    }
  }

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

  @Override
  public void unSchedule(Task task) {
    synchronized (this) {
      this.scheduledTasks.remove(task);
    }
  }

  @Override
  public void run(Runnable runnable) {
    this.taskFactory.create(0, 0, false, false, t -> runnable.run()).schedule();
  }

  @Override
  public void runAsync(Runnable runnable) {
    this.taskFactory.create(0, 0, true, false, t -> runnable.run()).schedule();
  }

  @Override
  public Task scheduleSync(int ticks, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, 0, false, false, t -> runnable.run());
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleSync(int ticks, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, 0, false, false, runnable);
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleAsync(int ticks, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, 0, true, false, t -> runnable.run());
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleAsync(int ticks, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, 0, true, false, runnable);
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleSyncRepeating(int ticks, int interval, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, interval, false, true, t -> runnable.run());
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleSyncRepeating(int ticks, int interval, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, interval, false, true, runnable);
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleAsyncRepeating(int ticks, int interval, Runnable runnable) {
    Task task = this.taskFactory.create(ticks, interval, true, true, t -> runnable.run());
    task.schedule();
    return task;
  }

  @Override
  public Task scheduleAsyncRepeating(int ticks, int interval, Consumer<Task> runnable) {
    Task task = this.taskFactory.create(ticks, interval, true, true, runnable);
    task.schedule();
    return task;
  }

  @Override
  public Set<Task> getTasks() {
    return this.scheduledTasks;
  }

  @Override
  public Set<Task> getSyncTasks() {
    return this.scheduledTasks.stream().filter(t -> !t.isAsync()).collect(Collectors.toSet());
  }

  @Override
  public Set<Task> getAsyncTasks() {
    return this.scheduledTasks.stream().filter(Task::isAsync).collect(Collectors.toSet());
  }

  @Override
  public Set<Task> getRepeatingTasks() {
    return this.scheduledTasks.stream().filter(Task::isRepeating).collect(Collectors.toSet());
  }

  @Override
  public Set<Task> getSyncRepeatingTasks() {
    return this.scheduledTasks.stream()
        .filter(t -> !t.isAsync() && t.isRepeating())
        .collect(Collectors.toSet());
  }

  @Override
  public Set<Task> getAsyncRepeatingTasks() {
    return this.scheduledTasks.stream()
        .filter(t -> t.isAsync() && t.isRepeating())
        .collect(Collectors.toSet());
  }
}
