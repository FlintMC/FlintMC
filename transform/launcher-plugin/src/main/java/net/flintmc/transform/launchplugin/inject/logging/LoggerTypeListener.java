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

package net.flintmc.transform.launchplugin.inject.logging;

import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import net.flintmc.framework.inject.logging.LoggingProvider;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

/** Provides injection for a {@link Logger}. */
public class LoggerTypeListener implements BiFunction<InjectionPoint, Key<Logger>, Logger> {

  public Logger apply(InjectionPoint injectionPoint, Key<Logger> loggerKey) {
    return InjectionHolder.getInjectedInstance(LoggingProvider.class)
        .getLogger(injectionPoint.getDeclaringType().getRawType());
  }
}
