package net.labyfy.component.inject;

import com.google.inject.Key;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Helper service to invoke methods using Guice injection.
 */
public interface InjectedInvocationHelper {
  /**
   * Invokes the given method on an instance of the declaring class fetched using Guice.
   *
   * @param method The method to invoke
   * @param <T>    The return type
   * @return The return value of the invoked method
   * @throws InvocationTargetException if the invoked method threw an exception.
   * @throws IllegalAccessException    if the method definition could not be accessed.
   */
  <T> T invokeMethod(Method method) throws InvocationTargetException, IllegalAccessException;

  /**
   * Invokes the given method on an instance of the declaring class fetched using Guice
   * with parameter bindings made available via the given map.
   *
   * @param method             The method to invoke
   * @param availableArguments Parameter bindings which should be used for invocation
   * @param <T>                The return type
   * @return The return value of the invoked method
   * @throws InvocationTargetException if the invoked method threw an exception.
   * @throws IllegalAccessException    if the method definition could not be accessed.
   */
  <T> T invokeMethod(Method method, Map<Key<?>, ?> availableArguments) throws InvocationTargetException, IllegalAccessException;

  /**
   * Invokes the given method on the given instance.
   *
   * @param method   The method to invoke
   * @param instance The instance to invoke the method on
   * @param <T>      The return type
   * @return The return value of the invoked method
   * @throws InvocationTargetException if the invoked method threw an exception.
   * @throws IllegalAccessException    if the method definition could not be accessed.
   */
  <T> T invokeMethod(Method method, Object instance) throws InvocationTargetException, IllegalAccessException;

  /**
   * Invokes the given method on the given instance with parameter bindings made available via
   * the given map.
   *
   * @param method             The method to invoke
   * @param instance           The instance to invoke the method on
   * @param availableArguments Parameter bindings which should be used for invocation
   * @param <T>                The return type
   * @return The return value of the invoked method
   * @throws InvocationTargetException if the invoked method threw an exception.
   * @throws IllegalAccessException    if the method definition could not be accessed.
   */
  <T> T invokeMethod(Method method, Object instance, Map<Key<?>, ?> availableArguments) throws InvocationTargetException, IllegalAccessException;
}
