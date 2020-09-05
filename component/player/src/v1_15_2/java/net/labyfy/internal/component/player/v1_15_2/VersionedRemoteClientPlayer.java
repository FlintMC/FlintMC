package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.HandSerializer;
import net.labyfy.component.player.serializer.util.HandSideSerializer;
import net.labyfy.component.player.serializer.util.PlayerClothingSerializer;
import net.labyfy.component.player.serializer.util.PoseSerializer;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
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
            HandSideSerializer handSideSerializer,
            GameProfileSerializer gameProfileSerializer,
            MinecraftComponentMapper minecraftComponentMapper,
            PlayerClothingSerializer playerClothingSerializer,
            PoseSerializer poseSerializer,
            SoundCategorySerializer soundCategorySerializer,
            SoundSerializer soundSerializer
    ) {
        super(
                handSerializer,
                handSideSerializer,
                gameProfileSerializer,
                minecraftComponentMapper,
                playerClothingSerializer,
                poseSerializer,
                soundCategorySerializer,
                soundSerializer
        );
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

    @Override
    public AbstractClientPlayerEntity getPlayer() {
        return this.clientPlayer;
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
