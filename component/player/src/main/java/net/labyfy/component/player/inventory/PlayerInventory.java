package net.labyfy.component.player.inventory;

import java.util.List;

/**
 * Represents a player inventory
 */
public interface PlayerInventory extends Inventory {

    /**
     * Retrieves armor contents of this player inventory
     *
     * @return the armor contents of this player inventory
     */
    // TODO: 02.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    List<Object> getArmorContents();

}
