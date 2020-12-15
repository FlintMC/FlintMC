package net.flintmc.framework.inject.assisted;

import com.google.common.collect.Lists;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.util.Classes;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class ConstructorMatcher {

  /**
   * Finds a constructor suitable for the method. If the implementation contained any constructor
   * marked with {@link AssistedInject}, this requires all {@link Assisted} parameters to exactly
   * match the parameters (in any order) listed in the method. Otherwise, if no {@link
   * AssistedInject} constructors exist, this will default to looking for an {@link Inject}
   * constructor.
   *
   * @param method The method for an suitable constructor.
   * @param returnType The return type of the method.
   * @param implementation The implementation.
   * @param parameters The parameters.
   * @param <T> The type of the method.
   * @return A suitable constructor for the method.
   * @throws ErrorsException Will be thrown if an error occurred while finding a constructor.
   */
  public <T> InjectionPoint findMatchingConstructorInjectionPoint(
      Method method, Key<?> returnType, TypeLiteral<T> implementation, List<Key<?>> parameters)
      throws ErrorsException {
    Errors errors = new Errors(method);
    if (returnType.getTypeLiteral().equals(implementation)) {
      errors = errors.withSource(implementation);
    } else {
      errors = errors.withSource(returnType).withSource(implementation);
    }

    Class<?> rawType = implementation.getRawType();
    if (Modifier.isInterface(rawType.getModifiers())) {
      errors.addMessage(
          "%s is an interface, not a concrete class.  Unable to create AssistedInject factory.",
          implementation);
      throw errors.toException();
    } else if (Modifier.isAbstract(rawType.getModifiers())) {
      errors.addMessage(
          "%s is abstract, not a concrete class.  Unable to create AssistedInject factory.",
          implementation);
      throw errors.toException();
    } else if (Classes.isInnerClass(rawType)) {
      errors.cannotInjectInnerClass(rawType);
      throw errors.toException();
    }

    Constructor<?> matchingConstructor = null;
    boolean anyAssistedInjectConstructors = false;

    for (Constructor<?> constructor : rawType.getDeclaredConstructors()) {
      if (constructor.isAnnotationPresent(AssistedInject.class)) {
        anyAssistedInjectConstructors = true;

        if (this.constructorHasMatchingParameters(
            implementation, constructor, parameters, errors)) {
          if (matchingConstructor != null) {
            errors.addMessage(
                "%s has more than one constructor annotated with @AssistedInject"
                    + " that matches the parameters in method %s.  Unable to create "
                    + "AssistedInject factory.",
                implementation, method);
            throw errors.toException();
          } else {
            matchingConstructor = constructor;
          }
        }
      }
    }

    if (!anyAssistedInjectConstructors) {
      try {
        return InjectionPoint.forConstructorOf(implementation);
      } catch (ConfigurationException e) {
        errors.merge(e.getErrorMessages());
        throw errors.toException();
      }
    } else {
      if (matchingConstructor != null) {
        @SuppressWarnings("unchecked")
        InjectionPoint injectionPoint =
            InjectionPoint.forConstructor(
                (Constructor<? super T>) matchingConstructor, implementation);
        return injectionPoint;
      } else {
        errors.addMessage(
            "%s has @AssistedInject constructors, but none of them match the"
                + " parameters in method %s.  Unable to create AssistedInject factory.",
            implementation, method);
        throw errors.toException();
      }
    }
  }

  /**
   * Matching logic for constructors annotation with {@link AssistedInject}.
   *
   * @return {@code true} if and only if all {@link Assisted} parameters in the constructor exactly
   *     match (in any order) all {@link Assisted} parameters the method's parameter.
   */
  private boolean constructorHasMatchingParameters(
      TypeLiteral<?> type, Constructor<?> constructor, List<Key<?>> parameterList, Errors errors)
      throws ErrorsException {
    List<TypeLiteral<?>> parameters = type.getParameterTypes(constructor);
    Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
    int parameterCount = 0;
    List<Key<?>> constructorKeys = Lists.newArrayList();
    for (TypeLiteral<?> parameter : parameters) {
      Key<?> parameterKey =
          Annotations.getKey(
              parameter, constructor, parameterAnnotations[parameterCount++], errors);
      constructorKeys.add(parameterKey);
    }

    return allMatch(constructorKeys, parameterList) && noneMatch(constructorKeys);
  }

  /**
   * @param constructorKeys A collection with all constructor keys.
   * @return {@code true} if none of the given keys match with the {@link Assisted} annotation,
   *     otherwise {@code false}.
   */
  private boolean noneMatch(List<Key<?>> constructorKeys) {
    return constructorKeys.stream().noneMatch(key -> key.getAnnotationType() == Assisted.class);
  }

  /**
   * @param constructorKeys A collection with all constructor keys.
   * @param parameterList A collection with all parameters keys.
   * @return {@code true} if all parameters match the provided constructor keys predicate, otherwise
   *     {@code false}.
   */
  private boolean allMatch(List<Key<?>> constructorKeys, List<Key<?>> parameterList) {
    return parameterList.stream().allMatch(constructorKeys::remove);
  }
}
