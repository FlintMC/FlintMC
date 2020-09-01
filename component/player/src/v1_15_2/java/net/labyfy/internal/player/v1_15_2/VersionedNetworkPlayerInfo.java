package net.labyfy.internal.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
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

    private final GameProfile gameProfile;
    private final ResourceLocationProvider resourceLocationProvider;

    /**
     * Initializes a versioned network player info
     *
     * @param gameProfile The game profile this player
     * @param provider The resource location provider
     */
    @Inject
    public VersionedNetworkPlayerInfo(GameProfile gameProfile, ResourceLocationProvider provider) {
        this.gameProfile = gameProfile;
        this.resourceLocationProvider = provider;
    }

    /**
     * Retrieves the game profile form the network information.
     *
     * @return the game profile of a player
     */
    @Override
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    /**
     * Retrieves the response time from the network information.
     *
     * @return the response time form the network information
     */
    @Override
    public int getResponseTime() {
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getResponseTime();
    }

    /**
     * Retrieves the player game mode from the network information.
     *
     * @return the player game mode
     */
    @Override
    public GameMode getGameMode() {
        return this.convertGameType(
                this.getPlayerInfo(
                        this.gameProfile.getUniqueId()
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
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getLastHealth();
    }

    /**
     * Retrieves the player display health.
     *
     * @return the player display health
     */
    @Override
    public int getDisplayHealth() {
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getDisplayHealth();
    }

    /**
     * Retrieves the player last health time.
     *
     * @return the player last health time
     */
    @Override
    public long getLastHealthTime() {
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getLastHealthTime();
    }

    /**
     * Retrieves the player health blink time.
     *
     * @return the player health blink time
     */
    @Override
    public long getHealthBlinkTime() {
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getHealthBlinkTime();
    }

    /**
     * Retrieves the player render visibility identifier.
     *
     * @return the player render visibility identifier
     */
    @Override
    public long getRenderVisibilityId() {
        return this.getPlayerInfo(this.gameProfile.getUniqueId()).getRenderVisibilityId();
    }

    /**
     * Retrieves the skin model of this player
     *
     * @return the skin model of this player
     */
    @Override
    public SkinModel getSkinModel() {
        return this.convertSkinType(this.getPlayerInfo(this.gameProfile.getUniqueId()).getSkinType());
    }

    /**
     * Retrieves the location of the player's skin
     *
     * @return the skin location
     */
    @Override
    public ResourceLocation getSkinLocation() {
        return this.resourceLocationProvider.get(this.getPlayerInfo(this.gameProfile.getUniqueId()).getLocationSkin().getPath());
    }

    /**
     * Retrieves the location of the player's cloak
     *
     * @return the cloak location
     */
    @Override
    public ResourceLocation getCloakLocation() {
        return this.resourceLocationProvider.get(/*this.getLocationCape().getPath()*/ null);
    }

    /**
     * Retrieves the location of the player's elytra
     *
     * @return the elytra location
     */
    @Override
    public ResourceLocation getElytraLocation() {
        return this.resourceLocationProvider.get(this.getPlayerInfo(this.gameProfile.getUniqueId()).getLocationElytra().getPath());
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
     * Coverts the game type to the labyfy game mode.
     *
     * @param type The game type to convert
     * @return the converted game mode or throws an {@link IllegalStateException}
     */
    private GameMode convertGameType(GameType type) {
        switch (type) {
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            case NOT_SET:
            default:
                throw new IllegalStateException("Unexpected value: " + type.name());

        }
    }

    /**
     * Converts the skin type to the labyfy skin model.
     *
     * @param type The skin type to convert
     * @return the converted skin model or throws an {@link IllegalStateException}
     */
    private SkinModel convertSkinType(String type) {
        switch (type) {
            case "default":
                return SkinModel.STEVE;
            case "slim":
                return SkinModel.ALEX;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private net.minecraft.client.network.play.NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        net.minecraft.client.network.play.NetworkPlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId);
        if (playerInfo == null) throw new NullPointerException("Exception handling..");

        return playerInfo;
    }

}
