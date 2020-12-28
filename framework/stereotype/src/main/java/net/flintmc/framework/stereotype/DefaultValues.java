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

package net.flintmc.framework.stereotype;

import java.util.HashMap;
import java.util.Map;

public class DefaultValues {

  private static final Map<String, String> DEFAULT_VALUES = new HashMap<>();

  static {
    registerDefault("byte", "(byte) 0");
    registerDefault("int", "(int) 0");
    registerDefault("short", "(short) 0");
    registerDefault("long", "(long) 0");

    registerDefault("double", "(double) 0.0");
    registerDefault("float", "(float) 0.0");

    registerDefault("boolean", "false");
    registerDefault("char", "' '");
    registerDefault("void", "");
  }

  private static void registerDefault(String className, String value) {
    DEFAULT_VALUES.put(className, value);
  }

  public static String getDefaultValue(String className) {
    return DEFAULT_VALUES.getOrDefault(className, "null");
  }
}
