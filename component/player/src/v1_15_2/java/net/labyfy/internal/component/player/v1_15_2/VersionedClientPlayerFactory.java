package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.ClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

/**
 * 1.15.2 implementation of {@link net.labyfy.component.player.ClientPlayer.Factory}
 */
@Singleton
@Implement(value = ClientPlayer.Factory.class, version = "1.15.2")
public class VersionedClientPlayerFactory implements ClientPlayer.Factory {

    /**
     * Creates a new {@link ClientPlayer}
     *
     * @return the created client player
     */
    @Override
    public ClientPlayer<AbstractClientPlayerEntity> create() {
        ClientPlayer player = InjectionHolder.getInjectedInstance(ClientPlayer.class);
        player.setPlayer(Minecraft.getInstance().player);
        return player;
    }
}
