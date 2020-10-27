package net.flintmc.mcapi.internal.chat.component;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTranslationComponent extends DefaultChatComponent implements TranslationComponent {

  private final List<ChatComponent> arguments = new ArrayList<>();
  private String translationKey;

  @Override
  public String translationKey() {
    return this.translationKey;
  }

  @Override
  public void translationKey(String translationKey) {
    this.translationKey = translationKey;
  }

  @Override
  public String translate() {
    if (this.translationKey == null) {
      return "null";
    }

    return this.translationKey; // TODO translate with the given translationKey (return the translationKey if no translation exists)
  }

  @Override
  public void arguments(ChatComponent... arguments) {
    this.arguments.clear();
    this.arguments.addAll(Arrays.asList(arguments));
  }

  @Override
  public void appendArgument(ChatComponent argument) {
    this.arguments.add(argument);
  }

  @Override
  public ChatComponent[] arguments() {
    return this.arguments.toArray(new ChatComponent[0]);
  }

  @Override
  public String getUnformattedText() {
    return this.translate();
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultTranslationComponent component = new DefaultTranslationComponent();
    component.translationKey = this.translationKey;
    this.arguments.forEach(argument -> component.arguments.add(argument.copy()));
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.translationKey == null && this.arguments.isEmpty();
  }
}
