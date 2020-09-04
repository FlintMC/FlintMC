package net.labyfy.internal.component.player.v1_15_2.inventory;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.inventory.PlayerInventory;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.List;

/**
 * 1.15.2 implementation of {@link PlayerInventory}
 */
@Implement(value = PlayerInventory.class, version = "1.15.2")
public class VersionedPlayerInventory extends VersionedInventory implements PlayerInventory {

    /**
     * Retrieves armor contents of this player inventory
     *
     * @return the armor contents of this player inventory
     */
    @Override
    public List<Object> getArmorContents() {
        return Collections.singletonList(Minecraft.getInstance().player.inventory.armorInventory);
    }
}
