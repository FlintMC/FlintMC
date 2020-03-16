package net.labyfy.component.inject;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class InjectionHolder {

  private final Collection<Runnable> initializationRunnables;
  private final Collection<Module> modules;
  private final AtomicReference<Injector> injectorReference;

  private static class Lazy {
    private static final InjectionHolder INSTANCE = new InjectionHolder();
  }

  private InjectionHolder() {
    this.initializationRunnables = new HashSet<>();
    this.modules = Sets.newConcurrentHashSet();
    this.injectorReference = new AtomicReference<>(null);
  }

  public synchronized InjectionHolder addModules(Module... modules) {
    System.out.println("Add");
    this.modules.addAll(Arrays.asList(modules));
    return this;
  }

  public synchronized Injector getInjector() {
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

  public void addInitializationListener(Runnable runnable){
    this.initializationRunnables.add(runnable);
  }

  public static void enableIngameState() {
    getInstance().initializationRunnables.forEach(Runnable::run);
  }

  public static InjectionHolder getInstance() {
    return Lazy.INSTANCE;
  }
}
