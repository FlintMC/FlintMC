package net.flintmc.mcapi.chat.internal.builder;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.*;

@Singleton
@Implement(ComponentBuilder.Factory.class)
public class DefaultComponentBuilderFactory implements ComponentBuilder.Factory {
  @Override
  public TextComponentBuilder text() {
    return new DefaultTextComponentBuilder();
  }

  @Override
  public KeybindComponentBuilder keybind() {
    return new DefaultKeybindComponentBuilder();
  }

  @Override
  public ScoreComponentBuilder score() {
    return new DefaultScoreComponentBuilder();
  }

  @Override
  public SelectorComponentBuilder selector() {
    return new DefaultSelectorComponentBuilder();
  }

  @Override
  public TranslationComponentBuilder translation() {
    return new DefaultTranslationComponentBuilder();
  }
}
