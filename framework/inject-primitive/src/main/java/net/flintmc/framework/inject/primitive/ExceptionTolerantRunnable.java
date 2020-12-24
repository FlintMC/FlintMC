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

package net.flintmc.framework.inject.primitive;

/**
 * The <code>ExceptionTolerantRunnable</code> interface should be implemented by any class whose
 * instances are intended to be executed by a thread. The class must define a method of no arguments
 * called <code>run</code>.
 *
 * <p>This interface is designed to provide a common protocol for objects that wish to execute code
 * while they are active. For example, <code>ExceptionTolerantRunnable</code> is implemented by
 * class <code>Thread</code>. Being active simply means that a thread has been started and has not
 * yet been stopped.
 *
 * <p>In addition, <code>ExceptionTolerantRunnable</code> provides the means for a class to be
 * active while not subclassing <code>Thread</code>. A class that implements <code>
 * ExceptionTolerantRunnable</code> can run without subclassing <code>Thread</code> by instantiating
 * a <code>Thread</code> instance and passing itself in as the target. In most cases, the <code>
 * ExceptionTolerantRunnable</code> interface should be used if you are only planning to override
 * the <code>run()</code> method and no other <code>Thread</code> methods. This is important because
 * classes should not be subclassed unless the programmer intends on modifying or enhancing the
 * fundamental behavior of the class.
 *
 * @see java.lang.Thread
 * @see java.util.concurrent.Callable
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface ExceptionTolerantRunnable {
  /**
   * When an object implementing interface <code>ExceptionTolerantRunnable</code> is used to create
   * a thread, starting the thread causes the object's <code>run</code> method to be called in that
   * separately executing thread.
   *
   * <p>The general contract of the method <code>run</code> is that it may take any action
   * whatsoever.
   *
   * @throws Exception If an exception occurs while executing the runnable.
   * @see java.lang.Thread#run()
   */
  void run() throws Exception;
}
