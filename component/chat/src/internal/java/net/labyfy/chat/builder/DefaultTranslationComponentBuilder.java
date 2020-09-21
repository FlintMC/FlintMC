package net.labyfy.chat.builder;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.DefaultTranslationComponent;
import net.labyfy.chat.component.TranslationComponent;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = TranslationComponentBuilder.class)
public class DefaultTranslationComponentBuilder
    extends DefaultComponentBuilder<TranslationComponentBuilder, TranslationComponent>
    implements TranslationComponentBuilder {

  public DefaultTranslationComponentBuilder() {
    super(DefaultTranslationComponent::new);
  }

  @Override
  public TranslationComponentBuilder translationKey(String translationKey) {
    super.currentComponent.translationKey(translationKey);
    return this;
  }

  @Override
  public String translationKey() {
    return super.currentComponent.translationKey();
  }

  @Override
  public TranslationComponentBuilder arguments(ChatComponent... arguments) {
    super.currentComponent.arguments(arguments);
    return this;
  }

  @Override
  public TranslationComponentBuilder appendArgument(ChatComponent argument) {
    super.currentComponent.appendArgument(argument);
    return this;
  }

  @Override
  public ChatComponent[] arguments() {
    return super.currentComponent.arguments();
  }
}
