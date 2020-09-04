package net.labyfy.internal.component.player.v1_15_2.inventory;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.inventory.Inventory;

import java.util.List;

/**
 * 1.15.2 implementation of {@link Inventory}
 */
@Implement(value = Inventory.class, version = "1.15.2")
public class VersionedInventory implements Inventory {

    /**
     * Retrieves the inventory size
     *
     * @return the inventory size
     */
    @Override
    public int getSize() {
        // TODO: 02.09.2020 Gets the size of the inventory
        return 0;
    }

    /**
     * Retrieves an item stack which is in the slot.
     *
     * @param slot The slot of the item stack
     * @return an item stack or {@code null}
     */
    @Override
    public Object getStackInSlot(int slot) {
        // TODO: 02.09.2020 Gets the item stack in an inventory which is in the slot
        return null;
    }

    /**
     * Removes an item stack which is in the slot.
     *
     * @param slot The slot of the item stack
     * @return a removed item stack or {@code null}
     */
    @Override
    public Object removeStackFromSlot(int slot) {
        // TODO: 02.09.2020 Removes an item stack from an inventory which is in the slot
        return null;
    }

    /**
     * A collection with all items in the inventory
     *
     * @return a collection with all item in the inventory
     */
    @Override
    public List<Object> getContents() {
        // TODO: 02.09.2020 Gets the content from an inventory
        return null;
    }
}
