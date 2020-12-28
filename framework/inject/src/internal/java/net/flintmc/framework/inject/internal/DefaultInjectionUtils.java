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

package net.flintmc.framework.inject.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

/** {@inheritDoc} */
@Singleton
@Implement(InjectionUtils.class)
public class DefaultInjectionUtils implements InjectionUtils {

  private final Logger logger;
  private final AtomicInteger idCounter;
  private final Random random;

  @Inject
  private DefaultInjectionUtils(@InjectLogger Logger logger) {
    this.logger = logger;
    this.idCounter = new AtomicInteger();
    this.random = new Random();
  }

  /** {@inheritDoc} */
  @Override
  public String generateInjectedFieldName() {
    return "injected_" + this.idCounter.getAndIncrement() + "_" + this.random.nextInt(99999);
  }

  /** {@inheritDoc} */
  @Override
  public CtField addInjectedField(
      CtClass declaringClass,
      String fieldName,
      String injectedTypeName,
      boolean singletonInstance,
      boolean staticField)
      throws CannotCompileException {
    if (declaringClass.isInterface()) {
      throw new IllegalArgumentException(
          "Cannot add fields to an interface: " + declaringClass.getName());
    }

    if (!singletonInstance) {
      for (CtField field : declaringClass.getDeclaredFields()) {
        try {
          if (!field.getType().getName().equals(injectedTypeName)) {
            continue;
          }
        } catch (NotFoundException exception) {
          this.logger.trace(
              String.format(
                  "Failed to find type of field %s.%s", declaringClass.getName(), field.getName()),
              exception);
          continue;
        }

        if (Modifier.isStatic(field.getModifiers()) == staticField) {
          return field;
        }
      }
    }

    CtField field =
        CtField.make(
            String.format(
                "private %s final %s %s = (%2$s) %s.getInjectedInstance(%2$s.class);",
                staticField ? "static" : "",
                injectedTypeName,
                fieldName,
                InjectionHolder.class.getName()),
            declaringClass);

    declaringClass.addField(field);

    return field;
  }
}
