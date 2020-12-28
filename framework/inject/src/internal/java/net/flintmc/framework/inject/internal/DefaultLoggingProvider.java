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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.LoggingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

/** Default implementation of the {@link LoggingProvider} */
@Singleton
@Implement(LoggingProvider.class)
public class DefaultLoggingProvider implements LoggingProvider {
  // Default prefix when no other prefix is available
  private static final String FLINT_PREFIX = "Flint";
  // Cached loggers to save memory and speed up execution
  private final Map<Class<?>, Logger> loggerCache;
  // Function to map classes to prefixes
  private Function<Class<?>, String> prefixProvider = (clazz) -> null;

  @Inject
  private DefaultLoggingProvider() {
    // The cache needs to be concurrent
    this.loggerCache = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger(Class<?> clazz) {
    return this.loggerCache.computeIfAbsent(clazz, this::createLogger);
  }

  /** {@inheritDoc} */
  @Override
  public void setPrefixProvider(Function<Class<?>, String> prefixProvider) {
    this.prefixProvider = prefixProvider;
  }

  /**
   * Creates a logger for the given class.
   *
   * @param clazz The class to create the logger for
   * @return The created logger
   */
  private Logger createLogger(Class<?> clazz) {
    return LogManager.getLogger(
        clazz,
        new AbstractMessageFactory() {
          /** {@inheritDoc} */
          @Override
          public Message newMessage(Object message) {
            String prefix = prefixProvider.apply(clazz);
            if (prefix == null) {
              return new ParameterizedMessage("[" + FLINT_PREFIX + " ]: {}", message);
            }

            return new ParameterizedMessage("[" + prefix + "]: {}", message);
          }

          /** {@inheritDoc} */
          @Override
          public Message newMessage(String message) {
            String prefix = prefixProvider.apply(clazz);
            if (prefix == null) {
              return new ParameterizedMessage("[" + FLINT_PREFIX + "]: {}", message);
            }

            return new ParameterizedMessage("[" + prefix + "]: {}", message);
          }

          /** {@inheritDoc} */
          @Override
          public Message newMessage(String message, Object... params) {
            String prefix = prefixProvider.apply(clazz);
            if (prefix == null) {
              return new ParameterizedMessage("[" + FLINT_PREFIX + "]: " + message, params);
            }

            return new ParameterizedMessage("[" + prefix + "]: " + message, params);
          }
        });
  }
}
