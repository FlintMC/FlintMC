package net.labyfy.internal.player.v1_15_2;

import com.google.inject.Inject;
import com.mojang.authlib.properties.Property;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.FoodStats;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.gameprofile.property.PropertyMap;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.internal.player.gameprofile.property.DefaultProperty;
import net.labyfy.internal.player.gameprofile.property.DefaultPropertyMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;

import java.util.Map;
import java.util.UUID;

/**
 * 1.15.2 implementation of a minecraft player.
 */
@Implement(value = Player.class, version = "1.15.2")
public class VersionedPlayer implements Player {

    private final GameProfile.Builder profileBuilder;
    private final FoodStats foodStats;
    private final ResourceLocationProvider resourceLocationProvider;

    /**
     * Initializes a versioned player
     *
     * @param profileBuilder The game profile builder for this player
     * @param resourceLocationProvider The resource location provider for this player
     * @param foodStats The food statistics for this player
     */
    @Inject
    public VersionedPlayer(
            GameProfile.Builder profileBuilder,
            ResourceLocationProvider resourceLocationProvider,
            FoodStats foodStats
    ) {
        this.profileBuilder = profileBuilder;
        this.foodStats = foodStats;
        this.resourceLocationProvider = resourceLocationProvider;
    }

    /**
     * Retrieves the game profile of this player.
     *
     * @return the game profile of this player.
     */
    @Override
    public GameProfile getGameProfile() {
        com.mojang.authlib.GameProfile gameProfile = Minecraft.getInstance().player.getGameProfile();

        PropertyMap properties = new DefaultPropertyMap();
        for (Map.Entry<String, Property> entry : gameProfile.getProperties().entries()) {
            properties.put(
                    entry.getKey(),
                    new DefaultProperty(
                            entry.getValue().getName(),
                            entry.getValue().getValue(),
                            entry.getValue().getSignature()
                    )
            );
        }

        return this.profileBuilder
                .setUniqueId(gameProfile.getId())
                .setName(gameProfile.getName())
                .setProperties(properties)
                .build();
    }

    /**
     * Retrieves the name of this player.
     *
     * @return the name of this player
     */
    @Override
    public Object getName() {
        return Minecraft.getInstance().player.getName();
    }

    /**
     * Retrieves the display name of this player
     *
     * @return the display name of this player
     */
    @Override
    public Object getDisplayName() {
        return Minecraft.getInstance().player.getDisplayName();
    }

    /**
     * Retrieves the unique identifier of this player.
     *
     * @return the unique identifier of this player
     */
    @Override
    public UUID getUniqueId() {
        return Minecraft.getInstance().player.getUniqueID();
    }

    /**
     * Retrieves the health of this player.
     *
     * @return the health of this player
     */
    @Override
    public float getHealth() {
        return Minecraft.getInstance().player.getHealth();
    }

    /**
     * Retrieves the experience level of this player.
     *
     * @return the experience level of this player
     */
    @Override
    public int getExperienceLevel() {
        return Minecraft.getInstance().player.experienceLevel;
    }

    /**
     * Retrieves the experience total of this player.
     *
     * @return the experience total of this player
     */
    @Override
    public int getExperienceTotal() {
        return Minecraft.getInstance().player.experienceTotal;
    }

    /**
     * Retrieves the experience of this player.
     *
     * @return the experience of this player.
     */
    @Override
    public float getExperience() {
        return Minecraft.getInstance().player.experience;
    }

    /**
     * Retrieves the x position of this player.
     *
     * @return the x position of this player
     */
    @Override
    public double getX() {
        return Minecraft.getInstance().player.getPosX();
    }

    /**
     * Retrieves the y position of this player.
     *
     * @return the y position of this player
     */
    @Override
    public double getY() {
        return Minecraft.getInstance().player.getPosY();
    }

    /**
     * Retrieves the z position of this player.
     *
     * @return the z position of this playe
     */
    @Override
    public double getZ() {
        return Minecraft.getInstance().player.getPosZ();
    }

    /**
     * Retrieves the pitch of this player.
     *
     * @return the pitch of this player.
     */
    @Override
    public float getPitch() {
        return Minecraft.getInstance().player.getPitchYaw().x;
    }

    /**
     * Retrieves the yaw of this player.
     *
     * @return the yaw of this player.
     */
    @Override
    public float getYaw() {
        return Minecraft.getInstance().player.getPitchYaw().y;
    }

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return the rotation yaw head of this player.
     */
    @Override
    public float getRotationYawHead() {
        return Minecraft.getInstance().player.getRotationYawHead();
    }

    /**
     * Whether the player is in spectator mode.
     *
     * @return {@code true} if the player is in the spectator mode, otherwise {@code false}
     */
    @Override
    public boolean isSpectator() {
        return Minecraft.getInstance().player.isSpectator();
    }

    /**
     * Whether the player is in creative mode.
     *
     * @return {@code true} if the player is in the creative mode, otherwise {@code false}
     */
    @Override
    public boolean isCreative() {
        return Minecraft.getInstance().player.isCreative();
    }

    /**
     * Whether the player has any information
     *
     * @return {@code true} if the player has any information, otherwise {@code false}
     */
    @Override
    public boolean hasPlayerInfo() {
        return Minecraft.getInstance().player.hasPlayerInfo();
    }

    /**
     * Retrieves the <b>FOV</b> modifier of this player.
     *
     * @return the fov modifier of this player
     */
    @Override
    public float getFovModifier() {
        return Minecraft.getInstance().player.getFovModifier();
    }

    /**
     * Retrieves the active hand of this player.
     *
     * @return the active hand of this player
     */
    @Override
    public Hand getActiveHand() {
        net.minecraft.util.Hand minecraftHand = Minecraft.getInstance().player.getActiveHand();
        Hand activeHand;

        switch (minecraftHand) {
            case MAIN_HAND:
                activeHand = Hand.MAIN_HAND;
                break;
            case OFF_HAND:
                activeHand = Hand.OFF_HAND;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + minecraftHand);
        }

        return activeHand;
    }

    /**
     * Whether a hand is active.
     *
     * @return {@code true} if a hand is active, otherwise {@code false}
     */
    @Override
    public boolean isHandActive() {
        return Minecraft.getInstance().player.isHandActive();
    }

    /**
     * Retrieves the active item stack of this player.
     *
     * @return the active item stack of this player
     */
    @Override
    public Object getActiveItemStack() {
        return Minecraft.getInstance().player.getActiveItemStack();
    }

    /**
     * Retrieves use count of the item.
     *
     * @return the use count of the item
     */
    @Override
    public int getItemInUseCount() {
        return Minecraft.getInstance().player.getItemInUseCount();
    }

    /**
     * Retrieves the maximal use count of the item.
     *
     * @return the maximal use count of this item.
     */
    @Override
    public int getItemInUseMaxCount() {
        return Minecraft.getInstance().player.getItemInUseMaxCount();
    }

    /**
     * Retrieves the food statistics of this player
     *
     * @return the food statistics of this player
     */
    @Override
    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    /**
     * Retrieves the network player info of this player.
     *
     * @return the network player info of this  player
     */
    @Override
    public NetworkPlayerInfo getNetworkPlayerInfo() {
        return new VersionedNetworkPlayerInfo(this.getGameProfile(), this.resourceLocationProvider);
    }

    /**
     * Sends a message to the chat
     *
     * @param component The message to be sent
     */
    @Override
    public void sendMessage(Object component) {

    }

    /**
     * Retrieves the skin model of this player
     *
     * @return the skin model of this player
     */
    @Override
    public SkinModel getSkinModel() {
        return this.getNetworkPlayerInfo().getSkinModel();
    }

    /**
     * Retrieves the location of the player's skin
     *
     * @return the skin location
     */
    @Override
    public ResourceLocation getSkinLocation() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo == null ? this.resourceLocationProvider.get(
                DefaultPlayerSkin.getDefaultSkin(this.getUniqueId()).getPath()
        ) : networkPlayerInfo.getSkinLocation();
    }

    /**
     * Retrieves the location of the player's cloak
     *
     * @return the cloak location
     */
    @Override
    public ResourceLocation getCloakLocation() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo == null ? null : networkPlayerInfo.getCloakLocation();
    }

    /**
     * Retrieves the location of the player's elytra
     *
     * @return the elytra location
     */
    @Override
    public ResourceLocation getElytraLocation() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo == null ? null : networkPlayerInfo.getElytraLocation();
    }

    /**
     * Whether the player has a skin.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasSkin() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo != null && networkPlayerInfo.hasSkin();
    }

    /**
     * Whether the player has a cloak.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasCloak() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo != null && networkPlayerInfo.hasCloak();
    }

    /**
     * Whether the player has a elytra.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasElytra() {
        NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
        return networkPlayerInfo != null && networkPlayerInfo.hasElytra();
    }

}
