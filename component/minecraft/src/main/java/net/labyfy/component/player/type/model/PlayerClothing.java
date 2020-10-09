package net.labyfy.component.player.type.model;

import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.primitive.InjectionHolder;

/**
 * An enumeration of all available articles of clothing.
 */
public enum PlayerClothing {

    /**
     * The cloak of this player.
     */
    CLOAK(0, "cape"),
    /**
     * The jacket of this player.
     */
    JACKET(1, "jacket"),
    /**
     * The left sleeve of this player.
     */
    LEFT_SLEEVE(2, "left_sleeve"),
    /**
     * The right sleeve of this player.
     */
    RIGHT_SLEEVE(3, "right_sleeve"),
    /**
     * The left pants leg of this player.
     */
    LEFT_PANTS_LEG(4, "left_pants_leg"),
    /**
     * The right pants leg of this player.
     */
    RIGHT_PANTS_LEG(5, "right_pants_leg"),
    /**
     * The hat of this player.
     */
    HAT(6, "hat");

    private final int clothingId;
    private final int clothingMask;
    private final String clothingName;
    private final ChatComponent name;

    PlayerClothing(int clothingId, String clothingName) {
        this.clothingId = clothingId;
        this.clothingMask = 1 << clothingId;
        this.clothingName = clothingName;
        this.name = InjectionHolder
                .getInjectedInstance(ComponentBuilder.Factory.class)
                .translation()
                .translationKey("options.modelPart." + clothingName)
                .build();;
    }

    /**
     * Retrieves the identifier of this clothing.
     *
     * @return The identifier of this clothing.
     */
    public int getClothingId() {
        return clothingId;
    }

    /**
     * Retrieves the mask of this clothing.
     *
     * @return The mask of this clothing.
     */
    public int getClothingMask() {
        return clothingMask;
    }

    /**
     * Retrieves the name of this clothing.
     *
     * @return The name of this clothing.
     */
    public String getClothingName() {
        return clothingName;
    }

    /**
     * Retrieves the translated name of this clothing.
     *
     * @return The translated name of this clothing.
     */
    public ChatComponent getName() {
        return name;
    }
}

