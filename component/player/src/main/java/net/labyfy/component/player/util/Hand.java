package net.labyfy.component.player.util;

import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.builder.TranslationComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.component.TranslationComponent;
import net.labyfy.component.inject.primitive.InjectionHolder;

/**
 * An enumeration of all available hands.
 */
public enum Hand {
    /**
     * The main-hand of a player
     */
    MAIN_HAND,
    /**
     * The off-hand of a player
     */
    OFF_HAND;

    /**
     * An enumeration of all available hand sides.
     */
    public enum Side {
        LEFT("options.mainHand.left"),
        RIGHT("options.mainHand.right");

        private final String translateKey;
        private final ChatComponent handName;

        Side(String translateKey) {
            this.translateKey = translateKey;
            this.handName = InjectionHolder
                    .getInjectedInstance(ComponentBuilder.Factory.class)
                    .translation()
                    .translationKey(translateKey)
                    .build();
        }

        /**
         * Retrieves the opposite hand.
         *
         * @return the opposite hand.
         */
        public Side opposite() {
            return this == LEFT ? RIGHT : LEFT;
        }

        /**
         * Retrieves the translation key of this hand side.
         *
         * @return the translation key of this hand side.
         */
        public String getTranslateKey() {
            return translateKey;
        }

        /**
         * Retrieves the translated name of this hand side.
         *
         * @return the translated name of this hand side.
         */
        public ChatComponent getHandName() {
            return handName;
        }
    }
}
