package net.labyfy.component.initializer.inject;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.initializer.inject.logging.AnnotatedLoggerTypeListener;
import net.labyfy.component.initializer.inject.logging.LoggerTypeListener;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.util.ContextAwareProvisionListener;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class InitializationModule extends AbstractModule {

  private final AtomicReference<Injector> injectorHolder;
  private final Map<String, String> launchArguments;

  private InitializationModule(
      AtomicReference<Injector> injectorHolder,
      Map<String, String> launchArguments) {
    this.injectorHolder = injectorHolder;
    this.launchArguments = launchArguments;
  }

  protected void configure() {
    this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(this.launchArguments);
    this.bind(Key.get(AtomicReference.class, Names.named("injectorReference")))
        .toInstance(this.injectorHolder);
    ContextAwareProvisionListener.bindContextAwareProvider(this.binder(), Key.get(Logger.class, InjectLogger.class), new AnnotatedLoggerTypeListener(this.injectorHolder));
    ContextAwareProvisionListener.bindContextAwareProvider(this.binder(), Logger.class, new LoggerTypeListener(this.injectorHolder));
  }


  public static InitializationModule create(
      AtomicReference<Injector> injectorHolder,
      Map<String, String> launchArguments) {
    Preconditions.checkNotNull(injectorHolder);
    Preconditions.checkNotNull(launchArguments);
    return new InitializationModule(injectorHolder, launchArguments);
  }
}
