package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.Player;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

/**
 * 1.15.2 implementation of {@link net.labyfy.component.player.Player.Factory}
 */
@Singleton
@Implement(value = Player.Factory.class, version = "1.15.2")
public class VersionedPlayerFactory implements Player.Factory<AbstractClientPlayerEntity> {

    /**
     * Creates a new {@link Player} with the given {@link AbstractClientPlayerEntity}.
     *
     * @param player The {@link AbstractClientPlayerEntity} to create this player
     * @return the created player
     */
    @Override
    public Player create(AbstractClientPlayerEntity player) {
        Player playerEntity = InjectionHolder.getInjectedInstance(Player.class);
        playerEntity.setPlayer(player);
        return playerEntity;
    }
}
