package net.labyfy.component.player;

import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;

/**
 * Represents all skin-related things of a player.
 */
public interface PlayerSkinProfile {

    /**
     * Retrieves the skin model of this player
     *
     * @return the skin model of this player
     */
    SkinModel getSkinModel();

    /**
     * Retrieves the location of the player's skin
     *
     * @return the skin location
     */
    ResourceLocation getSkinLocation();

    /**
     * Retrieves the location of the player's cloak
     *
     * @return the cloak location
     */
    ResourceLocation getCloakLocation();

    /**
     * Retrieves the location of the player's elytra
     *
     * @return the elytra location
     */
    ResourceLocation getElytraLocation();

    /**
     * Whether the player has a skin.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    boolean hasSkin();

    /**
     * Whether the player has a cloak.
     *
     * @return  {@code true} if this player has a skin, otherwise {@code false}
     */
    boolean hasCloak();

    /**
     * Whether the player has a elytra.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    boolean hasElytra();

}
