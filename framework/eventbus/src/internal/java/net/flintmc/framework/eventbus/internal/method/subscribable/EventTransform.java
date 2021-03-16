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

package net.flintmc.framework.eventbus.internal.method.subscribable;

import javassist.CtField;

class EventTransform {

  private final String eventClass;
  private final CtField methodsField;
  private final CtField unmodifiableMethodsField;
  private final CtField phasesField;

  public EventTransform(
      String eventClass,
      CtField methodsField,
      CtField unmodifiableMethodsField,
      CtField phasesField) {
    this.eventClass = eventClass;
    this.methodsField = methodsField;
    this.unmodifiableMethodsField = unmodifiableMethodsField;
    this.phasesField = phasesField;
  }

  public String getEventClass() {
    return this.eventClass;
  }

  public CtField getMethodsField() {
    return this.methodsField;
  }

  public CtField getUnmodifiableMethodsField() {
    return this.unmodifiableMethodsField;
  }

  public CtField getPhasesField() {
    return this.phasesField;
  }
}
