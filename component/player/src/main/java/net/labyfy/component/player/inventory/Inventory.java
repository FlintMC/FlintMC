package net.labyfy.component.player.inventory;

import java.util.List;

/**
 * Represents a inventory
 */
public interface Inventory {

    /**
     * Retrieves the inventory size
     *
     * @return the inventory size
     */
    int getSize();

    /**
     * Retrieves an item stack which is in the slot.
     *
     * @param slot The slot of the item stack
     * @return an item stack or {@code null}
     */
    // TODO: 02.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Object getStackInSlot(int slot);

    /**
     * Removes an item stack which is in the slot.
     *
     * @param slot The slot of the item stack
     * @return a removed item stack or {@code null}
     */
    // TODO: 02.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Object removeStackFromSlot(int slot);

    /**
     * A collection with all items in the inventory
     *
     * @return a collection with all item in the inventory
     */
    // TODO: 02.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    List<Object> getContents();

}
