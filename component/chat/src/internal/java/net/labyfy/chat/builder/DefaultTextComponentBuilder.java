package net.labyfy.chat.builder;

import net.labyfy.chat.component.DefaultTextComponent;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = TextComponentBuilder.class)
public class DefaultTextComponentBuilder
    extends DefaultComponentBuilder<TextComponentBuilder, TextComponent>
    implements TextComponentBuilder {

  public DefaultTextComponentBuilder() {
    super(DefaultTextComponent::new);
  }

  @Override
  public TextComponentBuilder text(String text) {
    super.currentComponent.text(text);
    return this;
  }

  @Override
  public TextComponentBuilder appendText(String text) {
    String currentText = this.text();
    return this.text(currentText == null ? text : currentText + text);
  }

  @Override
  public String text() {
    return super.currentComponent.text();
  }
}
