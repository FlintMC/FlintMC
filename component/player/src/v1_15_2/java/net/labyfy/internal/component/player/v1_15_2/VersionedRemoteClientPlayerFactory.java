package net.labyfy.internal.component.player.v1_15_2;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.RemoteClientPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

/**
 * 1.15.2 implementation of {@link net.labyfy.component.player.RemoteClientPlayer.Factory}
 */
@Implement(value = RemoteClientPlayer.Factory.class, version = "1.15.2")
public class VersionedRemoteClientPlayerFactory implements RemoteClientPlayer.Factory<AbstractClientPlayerEntity> {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given {@link AbstractClientPlayerEntity}.
     *
     * @param player The {@link AbstractClientPlayerEntity} to create this player.
     * @return a created remote client player
     */
    @Override
    public RemoteClientPlayer create(AbstractClientPlayerEntity player) {
        RemoteClientPlayer remoteClientPlayer = InjectionHolder.getInjectedInstance(RemoteClientPlayer.class);
        remoteClientPlayer.setPlayer(player);
        return remoteClientPlayer;
    }
}
