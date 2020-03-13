package net.labyfy.inject;

import com.google.inject.Injector;
import com.google.inject.Singleton;

/** @author Aventix created at: 03.07.2019 */
@Singleton
public class InjectionHolder {

  private static Injector injector;

  public static void setInjector(Injector injector) {
    InjectionHolder.injector = injector;
  }

  public static Injector getInjector() {
    return injector;
  }
}
