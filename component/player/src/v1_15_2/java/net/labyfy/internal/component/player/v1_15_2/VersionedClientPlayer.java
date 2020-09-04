package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.inventory.PlayerInventory;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.HandSerializer;
import net.labyfy.component.player.serializer.util.PoseSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.play.client.CChatMessagePacket;

import java.util.Collections;
import java.util.List;

/**
 * 1.15.2 implementation of a minecraft player.
 */
@Implement(value = ClientPlayer.class, version = "1.15.2")
public class VersionedClientPlayer extends VersionedPlayer implements ClientPlayer<AbstractClientPlayerEntity> {

    private ClientPlayerEntity clientPlayer;

    @Inject
    protected VersionedClientPlayer(
            HandSerializer handSerializer,
            GameProfileSerializer gameProfileSerializer,
            PoseSerializer poseSerializer
    ) {
        super(
                handSerializer,
                gameProfileSerializer,
                poseSerializer
        );
    }

    /**
     * Sets the {@link AbstractClientPlayerEntity}
     *
     * @param player The new player
     */
    @Override
    public void setPlayer(AbstractClientPlayerEntity player) {
        super.setPlayer(player);
        this.clientPlayer = (ClientPlayerEntity) player;
    }

    /**
     * Retrieves the inventory of this player.
     *
     * @return the inventory of this player
     */
    @Override
    public PlayerInventory getPlayerInventory() {
        return null;
    }

    /**
     * Retrieves the armor contents of this player.
     *
     * @return the armor contents of this player.
     */
    @Override
    public List<Object> getArmorContents() {
        return Collections.singletonList(this.clientPlayer.inventory.armorInventory);
    }

    /**
     * Retrieves the experience level of this player.
     *
     * @return the experience level of this player
     */
    @Override
    public int getExperienceLevel() {
        return this.clientPlayer.experienceLevel;
    }

    /**
     * Retrieves the experience total of this player.
     *
     * @return the experience total of this player
     */
    @Override
    public int getExperienceTotal() {
        return this.clientPlayer.experienceTotal;
    }

    /**
     * Retrieves the experience of this player.
     *
     * @return the experience of this player.
     */
    @Override
    public float getExperience() {
        return this.clientPlayer.experience;
    }

    /**
     * Retrieves the language of this player.
     *
     * @return the language of this player
     */
    @Override
    public String getLocale() {
        return Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getName();
    }

    /**
     * Retrieves the tab overlay of this player.
     *
     * @return the tab overlay of this player.
     */
    @Override
    public TabOverlay getTabOverlay() {
        return InjectionHolder.getInjectedInstance(TabOverlay.class);
    }

    /**
     * Whether the player has any information
     *
     * @return {@code true} if the player has any information, otherwise {@code false}
     */
    @Override
    public boolean hasPlayerInfo() {
        return this.getNetworkPlayerInfo() != null;
    }

    /**
     * Retrieves the <b>FOV</b> modifier of this player.
     *
     * @return the fov modifier of this player
     */
    @Override
    public float getFovModifier() {
        return this.clientPlayer.getFovModifier();
    }

    /**
     * Whether auto jump is enabled.
     *
     * @return {@code true} if auto jump is enabled, otherwise {@code false}
     */
    @Override
    public boolean isAutoJumpEnabled() {
        return this.clientPlayer.isAutoJumpEnabled();
    }

    /**
     * Whether the player rides a horse
     *
     * @return {@code true} if the player rides a horse, otherwise {@code false}
     */
    @Override
    public boolean isRidingHorse() {
        return this.clientPlayer.isRidingHorse();
    }

    /**
     * Whether the player rows a boat
     *
     * @return {@code true} if the player rows a boat, otherwise {@code false}
     */
    @Override
    public boolean isRowingBoat() {
        return this.clientPlayer.isRowingBoat();
    }

    /**
     * Whether the player's death screen is shown
     *
     * @return {@code true} if the player's death screen is shown, otherwise {@code false}
     */
    @Override
    public boolean isShowDeathScreen() {
        return this.clientPlayer.isShowDeathScreen();
    }

    /**
     * Retrieves the water brightness of this player.
     *
     * @return the water brightness of this player.
     */
    @Override
    public String getServerBrand() {
        return this.clientPlayer.getServerBrand();
    }

    /**
     * Retrieves the water brightness of this player.
     *
     * @return the water brightness of this player.
     */
    @Override
    public float getWaterBrightness() {
        return this.clientPlayer.getWaterBrightness();
    }

    /**
     * Retrieves the previous time in portal of this player.
     *
     * @return the previous time in portal of this player.
     */
    @Override
    public float getPrevTimeInPortal() {
        return this.clientPlayer.prevTimeInPortal;
    }

    /**
     * Retrieves the time in portal of this player.
     *
     * @return the time in portal of this player.
     */
    @Override
    public float getTimeInPortal() {
        return this.clientPlayer.timeInPortal;
    }

    /**
     * Performs a command
     *
     * @param command The command to perform
     * @return {@code true} if the command was performed, otherwise {@code false}
     */
    @Override
    public boolean performCommand(String command) {
        if (!command.startsWith("/")) return false;

        Minecraft.getInstance().getConnection().sendPacket(new CChatMessagePacket(command));

        return true;
    }

}
