package net.flintmc.render.gui.v1_16_4;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.screen.BuiltinScreenDisplayer;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.v1_16_4.lazy.VersionedBuiltinScreenDisplayInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

/** 1.16.4 Implementation of the {@link BuiltinScreenDisplayer} */
@Singleton
@Implement(BuiltinScreenDisplayer.class)
public class VersionedBuiltinScreenDisplayer implements BuiltinScreenDisplayer {
  private final Map<ScreenName, Consumer<Object[]>> supportedScreens;

  private boolean initialized;

  @Inject
  private VersionedBuiltinScreenDisplayer() {
    this.supportedScreens = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public boolean supports(ScreenName screenName) {
    if (!initialized) {
      VersionedBuiltinScreenDisplayInit.init(supportedScreens);
      initialized = true;
    }

    return supportedScreens.containsKey(screenName);
  }

  /** {@inheritDoc} */
  @Override
  public void display(ScreenName screenName, Object... args) {
    if (!supports(screenName)) {
      throw new UnsupportedOperationException(
          "This displayer does not support the screen" + screenName);
    }

    supportedScreens.get(screenName).accept(args);
  }

  @Override
  public ScreenName getOpenScreen() {
    Screen screen = Minecraft.getInstance().currentScreen;
    return screen != null ? ScreenName.unknown(screen.getClass().getName()) : null;
  }
}
