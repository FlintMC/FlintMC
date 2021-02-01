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

package net.flintmc.util.unittesting;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Optional;
import java.util.Random;
import net.flintmc.util.unittesting.RandomInt.Range;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

public class FlintJunitExtension implements TestInstanceFactory,
    ParameterResolver {


  @Override
  public Object createTestInstance(TestInstanceFactoryContext factoryContext,
      ExtensionContext extensionContext) throws TestInstantiationException {
    try {
      Object instance = factoryContext.getTestClass().newInstance();
      if (instance instanceof AbstractModule) {
        Injector injector = Guice.createInjector((AbstractModule) instance);
        injector.injectMembers(instance);
      }
      return instance;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.isAnnotated(RandomInt.class) && parameterContext
        .getParameter().getType().equals(int.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {

    Random random = extensionContext.getRoot().getStore(Namespace.GLOBAL)
        .getOrComputeIfAbsent(Random.class);

    Optional<RandomInt> optRandom = parameterContext
        .findAnnotation(RandomInt.class);
    if (!optRandom.isPresent()) {
      throw new ParameterResolutionException(
          "Parameter is not annotated with @RandomInt!");
    }

    RandomInt annotation = optRandom.get();

    if (annotation.value() == Range.NEGATIVE) {
      return -(random.nextInt(Integer.MAX_VALUE - 2) + 1);
    } else if (annotation.value() == Range.POSITIVE) {
      return random.nextInt(Integer.MAX_VALUE - 2);
    } else {
      return random.nextBoolean() ? -(random.nextInt(Integer.MAX_VALUE - 3) + 1)
          : random.nextInt(Integer.MAX_VALUE - 2);
    }
  }
}
