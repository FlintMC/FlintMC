package net.labyfy.internal.component.player.v1_15_2.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.player.world.Dimension;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.minecraft.client.Minecraft;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 1.15.2 implementation of {@link ClientWorld}
 */
@Singleton
@AutoLoad
@Implement(value = ClientWorld.class, version = "1.15.2")
public class VersionedClientWorld implements ClientWorld {

    private final net.minecraft.client.world.ClientWorld clientWorld;
    private final DimensionSerializer<DimensionType> dimensionSerializer;

    private final List<Player> players;

    @Inject
    public VersionedClientWorld(
            DimensionSerializer dimensionSerializer
    ) {
        this.dimensionSerializer = dimensionSerializer;
        this.clientWorld = Minecraft.getInstance().player.worldClient;
        this.players = new ArrayList<>();
    }

    /**
     * Retrieves the minecraft world.
     *
     * @return The minecraft world.
     */
    @Override
    public Object getClientWorld() {
        return this.clientWorld;
    }

    /**
     * Retrieves the time of this world.
     *
     * @return The time of this world.
     */
    @Override
    public long getTime() {
        return this.clientWorld.getDayTime();
    }

    /**
     * Retrieves the player count of this world.
     *
     * @return The player count of this world.
     */
    @Override
    public int getPlayerCount() {
        return this.clientWorld.getPlayers().size();
    }

    /**
     * Adds a player to the collection.
     *
     * @param player The player to add
     * @return {@code true} if this collection changed as a result of the call, otherwise {@code false}.
     */
    @Override
    public boolean addPlayer(Player player) {
        return this.players.add(player);
    }

    /**
     * Removes all of the players of this collection that satisfy the given predicate. Error or runtime
     * exception thrown during iteration or by the predicate are relayed to the caller.
     *
     * @param filter A predicate which returns {@code true} for players to removed
     * @return {@code true} if any players were removed
     */
    @Override
    public boolean removeIfPlayer(Predicate<? super Player> filter) {
        return this.players.removeIf(filter);
    }

    /**
     * Retrieves a collection with all players of this world.
     *
     * @return A collection with all players of this world.
     */
    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Retrieves the loaded entity count of this world.
     *
     * @return The loaded entity count of this world.
     */
    @Override
    public int getCountLoadedEntities() {
        return this.clientWorld.getCountLoadedEntities();
    }

    /**
     * Retrieves the type of this world.
     *
     * @return The type of this world.
     */
    @Override
    public Dimension getDimension() {
        return this.dimensionSerializer.deserialize(this.clientWorld.getDimension().getType());
    }

    /**
     * Retrieves the scoreboard of this world.
     *
     * @return The scoreboard of this world.
     */
    @Override
    public Object getScoreboard() {
        return this.clientWorld.getScoreboard();
    }


}
