package net.labyfy.internal.component.player.v1_15_2.world;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.World;
import net.labyfy.component.player.world.Dimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.15.2 implementation of {@link World}
 */
@Implement(value = World.class, version = "1.15.2")
public class VersionedWorld implements World {

    private final ClientWorld world;
    private final DimensionSerializer<DimensionType> dimensionSerializer;
    private final Player.Factory<AbstractClientPlayerEntity> playerFactory;

    @Inject
    public VersionedWorld(
            DimensionSerializer dimensionSerializer,
            Player.Factory playerFactory
    ) {
        this.dimensionSerializer = dimensionSerializer;
        this.playerFactory = playerFactory;
        this.world = Minecraft.getInstance().world;
    }

    /**
     * Retrieves the time of this world.
     *
     * @return the time of this world.
     */
    @Override
    public long getTime() {
        return this.world.getDayTime();
    }

    /**
     * Retrieves the player count of this world.
     *
     * @return the player count of this world.
     */
    @Override
    public int getPlayerCount() {
        return this.world.getPlayers().size();
    }

    /**
     * Retrieves a collection with all players of this world.
     *
     * @return a collection with all players of this world.
     */
    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        for (AbstractClientPlayerEntity player : this.world.getPlayers()) {
            players.add(this.playerFactory.create(player));
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
        return this.dimensionSerializer.deserialize(this.world.getDimension().getType());
    }
}
