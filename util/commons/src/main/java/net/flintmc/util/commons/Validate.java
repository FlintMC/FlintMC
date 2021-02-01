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

package net.flintmc.util.commons;

/**
 * Util class for validating of several arguments.
 */
public class Validate {

  /**
   * Checks whether the given {@code argument} is false and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @throws IllegalArgumentException If the {@code argument} is true
   */
  public static void checkFalse(boolean argument) {
    checkFalse(argument, null);
  }

  /**
   * Checks whether the given {@code argument} is false and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @param message  The message to be printed or {@code null} if the exception message shouldn't
   *                 contain any more information about the argument
   * @throws IllegalArgumentException If the {@code argument} is true
   */
  public static void checkFalse(boolean argument, String message) {
    if (argument) {
      throw new IllegalArgumentException(
          "Argument was true, but should be false" + (message != null ? ": " + message : ""));
    }
  }

  /**
   * Checks whether the given {@code argument} is true and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @throws IllegalArgumentException If the {@code argument} is false
   */
  public static void checkTrue(boolean argument) {
    checkTrue(argument, null);
  }

  /**
   * Checks whether the given {@code argument} is true and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @param message  The message to be printed or {@code null} if the exception message shouldn't
   *                 contain any more information about the argument
   * @throws IllegalArgumentException If the {@code argument} is false
   */
  public static void checkTrue(boolean argument, String message) {
    if (!argument) {
      throw new IllegalArgumentException(
          "Argument was false, but should be true" + (message != null ? ": " + message : ""));
    }
  }

  /**
   * Checks whether the given {@code argument} is not null and throws an exception if it is.
   *
   * @param argument The argument to check for
   * @throws IllegalArgumentException If the {@code argument} is null
   */
  public static <T> T checkNotNull(T argument) {
    return checkNotNull(argument, null);
  }

  /**
   * Checks whether the given {@code argument} is not null and throws an exception if it is.
   *
   * @param argument The argument to check for
   * @param message  The message to be printed or {@code null} if the exception message shouldn't
   *                 contain any more information about the argument
   * @throws IllegalArgumentException If the {@code argument} is null
   */
  public static <T> T checkNotNull(T argument, String message) {
    if (argument == null) {
      throw new IllegalArgumentException(
          "Value was null, but shouldn't be" + (message != null ? ": " + message : ""));
    }
    return argument;
  }

  /**
   * Checks whether the given {@code argument} is null and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @throws IllegalArgumentException If the {@code argument} is null
   */
  public static <T> T checkNull(T argument) {
    return checkNull(argument, null);
  }

  /**
   * Checks whether the given {@code argument} is null and throws an exception if it is not.
   *
   * @param argument The argument to check for
   * @param message  The message to be printed or {@code null} if the exception message shouldn't
   *                 contain any more information about the argument
   * @throws IllegalArgumentException If the {@code argument} is null
   */
  public static <T> T checkNull(T argument, String message) {
    if (argument == null) {
      throw new IllegalArgumentException(
          "Value was not null, but should be" + (message != null ? ": " + message : ""));
    }

    return argument;
  }

}
