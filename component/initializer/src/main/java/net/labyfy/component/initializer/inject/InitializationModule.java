package net.labyfy.component.initializer.inject;

import com.google.common.base.Preconditions;
import com.google.inject.*;
import com.google.inject.name.Names;
import net.labyfy.component.initializer.inject.logging.Log4JTypeListener;
import net.labyfy.component.inject.util.ContextAwareProvisionListener;
import net.labyfy.component.inject.logging.InjectLogger;
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
//    this.bindListener(Matchers.any(), new Log4JTypeListener(this.injectorHolder));

    ContextAwareProvisionListener.bindContextAwareProvider(this.binder(), Key.get(Logger.class, InjectLogger.class), new Log4JTypeListener(this.injectorHolder));
  }


  public static InitializationModule create(
      AtomicReference<Injector> injectorHolder,
      Map<String, String> launchArguments) {
    Preconditions.checkNotNull(injectorHolder);
    Preconditions.checkNotNull(launchArguments);
    return new InitializationModule(injectorHolder, launchArguments);
  }
}
