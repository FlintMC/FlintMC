package net.flintmc.mcapi.chat.internal.component;

import net.flintmc.mcapi.chat.component.TextComponent;

public class DefaultTextComponent extends DefaultChatComponent implements TextComponent {

  private String text;

  @Override
  public String text() {
    return this.text;
  }

  @Override
  public void text(String text) {
    this.text = text;
  }

  @Override
  public String getUnformattedText() {
    return this.text;
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultTextComponent component = new DefaultTextComponent();
    component.text = this.text;
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.text == null;
  }
}
