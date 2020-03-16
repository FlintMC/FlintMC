package net.labyfy.component.inject;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.TaskService;
import net.labyfy.component.tasks.Tasks;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
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

  public static void enableIngameState() {
    System.out.println("Ingame state " + InjectionHolder.class.getClassLoader());
    getInstance()
        .getInjector()
        .getInstance(TaskExecutor.class)
        .execute(Tasks.PRE_MINECRAFT_INITIALIZE);
  }

  public static InjectionHolder getInstance() {
    return Lazy.INSTANCE;
  }
}
