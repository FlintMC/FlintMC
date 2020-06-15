package net.labyfy.component.gui.v1_15_1;

import net.labyfy.component.gui.screen.BuiltinScreenDisplayer;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.gui.v1_15_1.lazy.LazyBuiltinScreenDisplayInit;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
@Implement(BuiltinScreenDisplayer.class)
public class LabyBuiltinScreenDisplayer implements BuiltinScreenDisplayer {
  private final Map<ScreenName, Consumer<Object[]>> supportedScreens;

  private boolean initialized;

  @Inject
  private LabyBuiltinScreenDisplayer() {
    this.supportedScreens = new HashMap<>();
  }

  @Override
  public boolean supports(ScreenName screenName) {
    if(!initialized) {
      LazyBuiltinScreenDisplayInit.init(supportedScreens);
      initialized = true;
    }

    return supportedScreens.containsKey(screenName);
  }

  @Override
  public void display(ScreenName screenName, Object... args) {
    if(!supports(screenName)) {
      throw new UnsupportedOperationException("This displayer does not support the screen" + screenName);
    }

    supportedScreens.get(screenName).accept(args);
  }
}
