package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.layout.component.base.JuklearTopLevelComponent;

import java.io.Closeable;
import java.util.Collection;

public interface JuklearMCScreen extends Closeable {
  Collection<JuklearTopLevelComponent> topLevelComponents();
  void updateSize(int width, int height);
  default void preNuklearRender() {}
  default void postNuklearRender() {}

  @Override
  default void close() {}
}
