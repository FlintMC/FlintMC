package net.labyfy.component.player.util;

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
        // TODO: 04.09.2020 Replaces the Object to TextComponent when the Chat API is ready
        private final Object handName;

        Side(String translateKey) {
            this.translateKey = translateKey;
            // TODO: 04.09.2020 Initializes a TranslatableComponent
            this.handName = null;
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
        public Object getHandName() {
            return handName;
        }
    }
}
