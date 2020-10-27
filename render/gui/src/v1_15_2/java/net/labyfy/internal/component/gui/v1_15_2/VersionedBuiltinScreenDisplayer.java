package net.labyfy.internal.component.gui.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.screen.BuiltinScreenDisplayer;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.gui.v1_15_2.lazy.VersionedBuiltinScreenDisplayInit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 1.15.2 Implementation of the {@link BuiltinScreenDisplayer}
 */
@Singleton
@Implement(BuiltinScreenDisplayer.class)
public class VersionedBuiltinScreenDisplayer implements BuiltinScreenDisplayer {
  private final Map<ScreenName, Consumer<Object[]>> supportedScreens;

  private boolean initialized;

  @Inject
  private VersionedBuiltinScreenDisplayer() {
    this.supportedScreens = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supports(ScreenName screenName) {
    if (!initialized) {
      VersionedBuiltinScreenDisplayInit.init(supportedScreens);
      initialized = true;
    }

    return supportedScreens.containsKey(screenName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void display(ScreenName screenName, Object... args) {
    if (!supports(screenName)) {
      throw new UnsupportedOperationException("This displayer does not support the screen" + screenName);
    }

    supportedScreens.get(screenName).accept(args);
  }
}
