package net.labyfy.component.inject.primitive;

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

  private static class Lazy {
    private static final InjectionHolder INSTANCE = new InjectionHolder();
  }

  private InjectionHolder() {
    this.modules = Sets.newConcurrentHashSet();
    this.injectorReference = new AtomicReference<>(null);
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

  public static <T> T getInjectedInstance(Key<T> key) {
    return getInstance().getInjector().getInstance(key);
  }

  public static <T> T getInjectedInstance(Class<T> clazz) {
    return getInjectedInstance(Key.get(clazz));
  }

  public static InjectionHolder getInstance() {
    return Lazy.INSTANCE;
  }
}
