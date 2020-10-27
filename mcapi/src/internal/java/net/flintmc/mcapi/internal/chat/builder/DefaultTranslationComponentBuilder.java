package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultTranslationComponent;

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
