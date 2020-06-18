package net.labyfy.component.gui.screen;

public interface BuiltinScreenDisplayer {
  boolean supports(ScreenName screenName);
  void display(ScreenName screenName, Object ...args);
}
