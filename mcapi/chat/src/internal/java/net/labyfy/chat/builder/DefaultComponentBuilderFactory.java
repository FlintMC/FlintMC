package net.labyfy.chat.builder;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;

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
