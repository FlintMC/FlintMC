package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.HandSerializer;
import net.labyfy.component.player.serializer.util.PoseSerializer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;

/**
 * 1.15.2 implementation of {@link RemoteClientPlayer}
 */
@Implement(value = RemoteClientPlayer.class, version = "1.15.2")
public class VersionedRemoteClientPlayer extends VersionedPlayer implements RemoteClientPlayer<AbstractClientPlayerEntity> {

    private RemoteClientPlayerEntity clientPlayer;

    @Inject
    protected VersionedRemoteClientPlayer(
            HandSerializer handSerializer,
            GameProfileSerializer gameProfileSerializer,
            PoseSerializer poseSerializer
    ) {
        super(handSerializer, gameProfileSerializer, poseSerializer);
    }

    /**
     * Sets the {@link AbstractClientPlayerEntity}
     *
     * @param player The new player.
     */
    @Override
    public void setPlayer(AbstractClientPlayerEntity player) {
        super.setPlayer(player);
        this.clientPlayer = (RemoteClientPlayerEntity) player;
    }

    /**
     * Whether the player is in the range to render.
     *
     * @param distance The distance
     * @return {@code true} if the player is in range, otherwise {@code false}
     */
    @Override
    public boolean isInRangeToRender(double distance) {
        return this.clientPlayer.isInRangeToRenderDist(distance);
    }
}
