package net.labyfy.internal.component.player.v1_15_2.world;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.player.world.Dimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;

/**
 * 1.15.2 implementation of {@link ClientWorld}
 */
@Implement(value = ClientWorld.class, version = "1.15.2")
public class VersionedClientWorld implements ClientWorld {

    private final net.minecraft.client.world.ClientWorld clientWorld;
    private final DimensionSerializer<DimensionType> dimensionSerializer;
    private final ClientPlayer.Factory clientPlayerFactory;
    private final RemoteClientPlayer.Factory<AbstractClientPlayerEntity> remotePlayerFactory;

    @Inject
    public VersionedClientWorld(
            DimensionSerializer dimensionSerializer,
            ClientPlayer.Factory clientPlayerFactory,
            RemoteClientPlayer.Factory remoteClientPlayerFactory
    ) {
        this.dimensionSerializer = dimensionSerializer;
        this.clientPlayerFactory = clientPlayerFactory;
        this.clientWorld = Minecraft.getInstance().world;
        this.remotePlayerFactory = remoteClientPlayerFactory;
    }

    /**
     * Retrieves the minecraft world.
     *
     * @return the minecraft world.
     */
    @Override
    public Object getMinecraftWorld() {
        return this.clientWorld;
    }

    /**
     * Retrieves the time of this world.
     *
     * @return the time of this world.
     */
    @Override
    public long getTime() {
        return this.clientWorld.getDayTime();
    }

    /**
     * Retrieves the player count of this world.
     *
     * @return the player count of this world.
     */
    @Override
    public int getPlayerCount() {
        return this.clientWorld.getPlayers().size();
    }

    /**
     * Retrieves a collection with all players of this world.
     *
     * @return a collection with all players of this world.
     */
    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        for (AbstractClientPlayerEntity player : this.clientWorld.getPlayers()) {
            if(player instanceof ClientPlayerEntity) {
                players.add(this.clientPlayerFactory.create());
            } else if(player instanceof RemoteClientPlayerEntity) {
                players.add(this.remotePlayerFactory.create(player));
            }
        }
        return players;
    }

    /**
     * Retrieves the type of this world.
     *
     * @return the type of this world.
     */
    @Override
    public Dimension getDimension() {
        return this.dimensionSerializer.deserialize(this.clientWorld.getDimension().getType());
    }

    /**
     * Retrieves the scoreboard of this world.
     *
     * @return the scoreboard of this world.
     */
    @Override
    public Object getScoreboard() {
        return this.clientWorld.getScoreboard();
    }

}
