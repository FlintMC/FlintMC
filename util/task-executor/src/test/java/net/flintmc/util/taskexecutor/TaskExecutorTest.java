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

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.flintmc.mcapi.event.TickEvent;
import net.flintmc.mcapi.event.TickEvent.Type;
import net.flintmc.util.taskexecutor.internal.DefaultTaskExecutor;
import net.flintmc.util.unittesting.FlintTest;
import net.flintmc.util.unittesting.RandomInt;
import net.flintmc.util.unittesting.RandomInt.Range;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@FlintTest
public class TaskExecutorTest extends AbstractModule {

  @Inject
  DefaultTaskExecutor taskExecutor;

  @Test
  public void testTaskScheduled() {
    Task task = this.mockTask(10);
    this.taskExecutor.schedule(task);
    assertTrue(this.taskExecutor.getTasks().contains(task));
    assertTrue(this.taskExecutor.getSyncTasks().contains(task));
  }

  @Test
  public void testTaskTicksDecrease(@RandomInt(Range.POSITIVE) int ticks) {
    ticks++;

    TickEvent tickEvent = mock(TickEvent.class);
    when(tickEvent.getType()).thenReturn(Type.GENERAL);

    Task task = mockTask(ticks);

    this.taskExecutor.schedule(task);
    taskExecutor.onTick(tickEvent);

    verify(task).setTicksToStart(ticks - 1);
  }

  @Test
  public void testTaskExecution() {
    TickEvent tickEvent = mock(TickEvent.class);
    when(tickEvent.getType()).thenReturn(Type.GENERAL);

    Task task = mockTask(0);
    this.taskExecutor.schedule(task);
    this.taskExecutor.onTick(tickEvent);

    verify(task, atLeastOnce()).run();
    verify(task, atMostOnce()).run();
  }

  private Task mockTask(int ticks) {
    Task task = mock(Task.class);
    when(task.getTicksToStart()).thenReturn(ticks);
    when(task.getInterval()).thenReturn(0);
    when(task.isAsync()).thenReturn(false);
    when(task.isRepeating()).thenReturn(false);
    when(task.isScheduled()).thenReturn(false);
    return task;
  }

  @Provides
  public Task.Factory provideTaskFactory() {
    return mock(Task.Factory.class);
  }

  @Provides
  public ExecutorService provideExecutorService() {
    return mock(ExecutorService.class);
  }

}
