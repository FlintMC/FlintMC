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

package net.flintmc.metaprogramming;


public enum ElementKind {
  PACKAGE,
  ENUM,
  CLASS,
  ANNOTATION_TYPE,
  INTERFACE,
  ENUM_CONSTANT,
  FIELD,
  PARAMETER,
  LOCAL_VARIABLE,
  EXCEPTION_PARAMETER,
  METHOD,
  CONSTRUCTOR,
  STATIC_INIT,
  INSTANCE_INIT,
  TYPE_PARAMETER,
  OTHER,
  RESOURCE_VARIABLE,
  MODULE;

  private ElementKind() {
  }

  public boolean isClass() {
    return this == CLASS || this == ENUM;
  }

  public boolean isInterface() {
    return this == INTERFACE || this == ANNOTATION_TYPE;
  }

  public boolean isField() {
    return this == FIELD || this == ENUM_CONSTANT;
  }
}
