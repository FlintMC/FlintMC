package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.chat.component.DefaultTextComponent;

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
