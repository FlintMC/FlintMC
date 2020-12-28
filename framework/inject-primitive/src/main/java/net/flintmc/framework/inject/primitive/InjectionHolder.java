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

package net.flintmc.framework.inject.primitive;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public class InjectionHolder {

  private final Collection<Module> modules;
  private final AtomicReference<Injector> injectorReference;

  private InjectionHolder() {
    this.modules = Sets.newConcurrentHashSet();
    this.injectorReference = new AtomicReference<>(null);
  }

  public static <T> T getInjectedInstance(Key<T> key) {
    return getInstance().getInjector().getInstance(key);
  }

  public static <T> T getInjectedInstance(Class<T> clazz) {
    return getInjectedInstance(Key.get(clazz));
  }

  public static InjectionHolder getInstance() {
    return Lazy.INSTANCE;
  }

  public InjectionHolder addModules(Module... modules) {
    this.modules.addAll(Arrays.asList(modules));
    return this;
  }

  public Injector getInjector() {
    if (this.injectorReference.get() == null) {
      Module[] modules = this.modules.toArray(new Module[] {});
      this.modules.clear();
      this.injectorReference.set(Guice.createInjector(modules));
    } else if (!this.modules.isEmpty()) {
      Module[] modules = this.modules.toArray(new Module[] {});
      this.modules.clear();
      this.injectorReference.set(this.injectorReference.get().createChildInjector(modules));
    }

    return this.injectorReference.get();
  }

  public AtomicReference<Injector> getInjectorReference() {
    return injectorReference;
  }

  private static class Lazy {
    private static final InjectionHolder INSTANCE = new InjectionHolder();
  }
}
