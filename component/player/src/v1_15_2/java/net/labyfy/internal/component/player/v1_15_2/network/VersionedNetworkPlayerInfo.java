package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;

import java.util.UUID;

/**
 * 1.15.2 implementation of the {@link NetworkPlayerInfo}
 */
@Implement(value = NetworkPlayerInfo.class, version = "1.15.2")
public class VersionedNetworkPlayerInfo implements NetworkPlayerInfo {

    private final Player.Factory player;
    private final GameModeSerializer<GameType> gameModeSerializer;
    private final ResourceLocationProvider resourceLocationProvider;
    private final SkinModelSerializer<String> skinModelSerializer;

    @Inject
    public VersionedNetworkPlayerInfo(
            Player.Factory player,
            GameModeSerializer gameModeSerializer,
            ResourceLocationProvider provider,
            SkinModelSerializer skinModelSerializer
    ) {
        this.player = player;
        this.gameModeSerializer = gameModeSerializer;
        this.resourceLocationProvider = provider;
        this.skinModelSerializer = skinModelSerializer;
    }

    /**
     * Retrieves the game profile form the network information.
     *
     * @return the game profile of a player
     */
    @Override
    public GameProfile getGameProfile() {
        return this.get().getGameProfile();
    }

    /**
     * Retrieves the response time from the network information.
     *
     * @return the response time form the network information
     */
    @Override
    public int getResponseTime() {
        return this.getPlayerInfo(this.get().getUniqueId()).getResponseTime();
    }

    /**
     * Retrieves the player game mode from the network information.
     *
     * @return the player game mode
     */
    @Override
    public GameMode getGameMode() {
        return this.gameModeSerializer.deserialize(
                this.getPlayerInfo(
                        this.get().getUniqueId()
                ).getGameType()
        );
    }

    /**
     * Retrieves the player last health.
     *
     * @return the player last health
     */
    @Override
    public int getLastHealth() {
        return this.getPlayerInfo(this.get().getUniqueId()).getLastHealth();
    }

    /**
     * Retrieves the player display health.
     *
     * @return the player display health
     */
    @Override
    public int getDisplayHealth() {
        return this.getPlayerInfo(this.get().getUniqueId()).getDisplayHealth();
    }

    /**
     * Retrieves the player last health time.
     *
     * @return the player last health time
     */
    @Override
    public long getLastHealthTime() {
        return this.getPlayerInfo(this.get().getUniqueId()).getLastHealthTime();
    }

    /**
     * Retrieves the player health blink time.
     *
     * @return the player health blink time
     */
    @Override
    public long getHealthBlinkTime() {
        return this.getPlayerInfo(this.get().getUniqueId()).getHealthBlinkTime();
    }

    /**
     * Retrieves the player render visibility identifier.
     *
     * @return the player render visibility identifier
     */
    @Override
    public long getRenderVisibilityId() {
        return this.getPlayerInfo(this.get().getUniqueId()).getRenderVisibilityId();
    }

    /**
     * Retrieves the skin model of this player
     *
     * @return the skin model of this player
     */
    @Override
    public SkinModel getSkinModel() {
        return this.skinModelSerializer.deserialize(
                this.getPlayerInfo(
                        this.get().getUniqueId()
                ).getSkinType()
        );
    }

    /**
     * Retrieves the location of the player's skin
     *
     * @return the skin location
     */
    @Override
    public ResourceLocation getSkinLocation() {
        return this.resourceLocationProvider.get(
                this.getPlayerInfo(
                        this.get().getUniqueId()
                ).getLocationSkin().getPath()
        );
    }

    /**
     * Retrieves the location of the player's cloak
     *
     * @return the cloak location
     */
    @Override
    public ResourceLocation getCloakLocation() {
        return this.resourceLocationProvider.get(
                this.getPlayerInfo(
                        this.get().getUniqueId()
                ).getLocationCape().getPath()
        );
    }

    /**
     * Retrieves the location of the player's elytra
     *
     * @return the elytra location
     */
    @Override
    public ResourceLocation getElytraLocation() {
        return this.resourceLocationProvider.get(
                this.getPlayerInfo(
                        this.get().getUniqueId()
                ).getLocationElytra().getPath()
        );
    }

    /**
     * Whether the player has a skin.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasSkin() {
        return this.getSkinLocation() != null;
    }

    /**
     * Whether the player has a cloak.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasCloak() {
        return this.getCloakLocation() != null;
    }

    /**
     * Whether the player has a elytra.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasElytra() {
        return this.getElytraLocation() != null;
    }

    /**
     * Retrieves a {@link net.minecraft.client.network.play.NetworkPlayerInfo} with the given unique identifier.
     *
     * @param uniqueId The unique identifier of the profile
     * @return a {@link net.minecraft.client.network.play.NetworkPlayerInfo} or {@code null}
     */
    private net.minecraft.client.network.play.NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        return Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId);
    }

    /**
     * Retrieves the player of this client.
     *
     * @return the client player
     */
    private Player get() {
        return this.player.create(Minecraft.getInstance().player);
    }

}
