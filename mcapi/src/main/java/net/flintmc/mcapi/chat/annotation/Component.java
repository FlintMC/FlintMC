package net.flintmc.mcapi.chat.annotation;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be used to declare a {@link ChatComponent} in an annotation.
 *
 * @see ComponentAnnotationSerializer
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

  /**
   * The text for the component. The {@link ComponentAnnotationSerializer} will use this either for
   * the {@link ComponentSerializer.Factory#legacy()} or, if {@link #translate()} is {@code true},
   * use it as a translationKey for a {@link TranslationComponent}.
   *
   * @return The text for the component
   */
  String value();

  /**
   * Whether the {@link #value()} should be translated or not.
   *
   * @return {@code true} if it should be translated, {@code false} otherwise
   */
  boolean translate() default false;
}
