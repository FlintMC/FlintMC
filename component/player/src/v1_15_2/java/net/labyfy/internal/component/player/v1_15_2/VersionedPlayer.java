package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.HandSerializer;
import net.labyfy.component.player.serializer.util.HandSideSerializer;
import net.labyfy.component.player.serializer.util.PlayerClothingSerializer;
import net.labyfy.component.player.serializer.util.PoseSerializer;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.EntityPose;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.player.world.World;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;

import java.util.UUID;

/**
 * 1.15.2 implementation of {@link Player}
 */
@Implement(value = Player.class, version = "1.15.2")
public class VersionedPlayer implements Player<AbstractClientPlayerEntity> {

    private AbstractClientPlayerEntity player;

    private final HandSerializer<net.minecraft.util.Hand> handSerializer;
    private final HandSideSerializer<HandSide> handSideSerializer;
    private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
    private final PlayerClothingSerializer<PlayerModelPart> playerClothingSerializer;
    private final PoseSerializer<Pose> poseSerializer;
    private final SoundCategorySerializer<net.minecraft.util.SoundCategory> soundCategorySerializer;
    private final SoundSerializer<SoundEvent> soundSerializer;

    @Inject
    protected VersionedPlayer(
            HandSerializer handSerializer,
            HandSideSerializer handSideSerializer,
            GameProfileSerializer gameProfileSerializer,
            PlayerClothingSerializer playerClothingSerializer,
            PoseSerializer poseSerializer,
            SoundCategorySerializer soundCategorySerializer,
            SoundSerializer soundSerializer
    ) {
        this.handSerializer = handSerializer;
        this.handSideSerializer = handSideSerializer;
        this.gameProfileSerializer = gameProfileSerializer;
        this.playerClothingSerializer = playerClothingSerializer;
        this.poseSerializer = poseSerializer;
        this.soundCategorySerializer = soundCategorySerializer;
        this.soundSerializer = soundSerializer;
        this.player = null;
    }

    /**
     * Sets the {@link AbstractClientPlayerEntity}
     *
     * @param player The new player
     */
    @Override
    public void setPlayer(AbstractClientPlayerEntity player) {
        this.player = player;
    }

    /**
     * Retrieves the world of this player.
     *
     * @return the world of this player.
     */
    @Override
    public World getWorld() {
        return InjectionHolder.getInjectedInstance(World.class);
    }

    /**
     * Retrieves the game profile of this player.
     *
     * @return the game profile of this player.
     */
    @Override
    public GameProfile getGameProfile() {
        return this.gameProfileSerializer.deserialize(this.player.getGameProfile());
    }

    /**
     * Retrieves the name of this player.
     *
     * @return the name of this player
     */
    // TODO: 04.09.2020 Replaces the Object to TextComponent when the Chat API is ready 
    @Override
    public Object getName() {
        return this.player.getName();
    }

    /**
     * Retrieves the display name of this player
     *
     * @return the display name of this player
     */
    // TODO: 04.09.2020 Replaces the Object to TextComponent when the Chat API is ready 
    @Override
    public Object getDisplayName() {
        return this.player.getDisplayName();
    }

    /**
     * Retrieves the unique identifier of this player.
     *
     * @return the unique identifier of this player
     */
    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueID();
    }

    /**
     * Retrieves the health of this player.
     *
     * @return the health of this player
     */
    @Override
    public float getHealth() {
        return this.player.getHealth();
    }

    /**
     * Retrieves the list name of this player.
     *
     * @return the list name of this player.
     */
    @Override
    public String getPlayerListName() {
        return this.player.getScoreboardName();
    }

    /**
     * Retrieves the world time.
     *
     * @return the world time.
     */
    @Override
    public long getPlayerTime() {
        return this.getWorld().getTime();
    }

    /**
     * Retrieves the x position of this player.
     *
     * @return the x position of this player
     */
    @Override
    public double getX() {
        return this.player.getPosX();
    }

    /**
     * Retrieves the y position of this player.
     *
     * @return the y position of this player
     */
    @Override
    public double getY() {
        return this.player.getPosY();
    }

    /**
     * Retrieves the z position of this player.
     *
     * @return the z position of this playe
     */
    @Override
    public double getZ() {
        return this.player.getPosZ();
    }

    /**
     * Retrieves the pitch of this player.
     *
     * @return the pitch of this player.
     */
    @Override
    public float getPitch() {
        return this.player.getPitchYaw().x;
    }

    /**
     * Retrieves the pitch of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return the pitch of this player.
     */
    @Override
    public float getPitch(float partialTicks) {
        return this.player.getPitch(partialTicks);
    }

    /**
     * Retrieves the yaw of this player.
     *
     * @return the yaw of this player.
     */
    @Override
    public float getYaw() {
        return this.player.getPitchYaw().y;
    }

    /**
     * Retrieves the yaw of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return the yaw of this player.
     */
    @Override
    public float getYaw(float partialTicks) {
        return this.player.getYaw(partialTicks);
    }

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return the rotation yaw head of this player.
     */
    @Override
    public float getRotationYawHead() {
        return this.player.getRotationYawHead();
    }

    /**
     * Retrieves the eye height of this player.
     *
     * @return the eye height of this player.
     */
    @Override
    public float getEyeHeight() {
        return this.player.getEyeHeight();
    }

    /**
     * Retrieves the eye height of this player.
     *
     * @param entityPose The current pose for the eye height
     * @return the eye height of this player
     */
    @Override
    public float getEyeHeight(EntityPose entityPose) {
        return this.player.getEyeHeight(this.poseSerializer.serialize(entityPose));
    }

    /**
     * Retrieves the y position of the eyes of this player.
     *
     * @return the y position of the eyes of this player
     */
    @Override
    public double getPosYEye() {
        return this.player.getPosYEye();
    }

    /**
     * Retrieves the pose of this player.
     *
     * @return the pose of this player.
     */
    @Override
    public EntityPose getPose() {
        return this.poseSerializer.deserialize(this.player.getPose());
    }

    /**
     * Whether the player is in spectator mode.
     *
     * @return {@code true} if the player is in the spectator mode, otherwise {@code false}
     */
    @Override
    public boolean isSpectator() {
        return this.player.isSpectator();
    }

    /**
     * Whether the player is in creative mode.
     *
     * @return {@code true} if the player is in the creative mode, otherwise {@code false}
     */
    @Override
    public boolean isCreative() {
        return this.player.isCreative();
    }

    /**
     * Retrieves the active hand of this player.
     *
     * @return the active hand of this player
     */
    @Override
    public Hand getActiveHand() {
        return this.handSerializer.deserialize(this.player.getActiveHand());
    }

    /**
     * Whether a hand is active.
     *
     * @return {@code true} if a hand is active, otherwise {@code false}
     */
    @Override
    public boolean isHandActive() {
        return this.player.isHandActive();
    }

    /**
     * Retrieves the active item stack of this player.
     *
     * @return the active item stack of this player
     */
    // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready 
    @Override
    public Object getActiveItemStack() {
        return this.player.getActiveItemStack();
    }

    /**
     * Retrieves use count of the item.
     *
     * @return the use count of the item
     */
    @Override
    public int getItemInUseCount() {
        return this.player.getItemInUseCount();
    }

    /**
     * Retrieves the maximal use count of the item.
     *
     * @return the maximal use count of this item.
     */
    @Override
    public int getItemInUseMaxCount() {
        return this.player.getItemInUseMaxCount();
    }

    /**
     * Retrieves the food level of this player.
     *
     * @return the food level of this player.
     */
    @Override
    public int getFoodLevel() {
        return this.player.getFoodStats().getFoodLevel();
    }

    /**
     * Retrieves the saturation of this player.
     *
     * @return the saturation of this player.
     */
    @Override
    public float getSaturationLevel() {
        return this.player.getFoodStats().getSaturationLevel();
    }

    /**
     * Whether the player needs food
     *
     * @return {@code true} if the player needs food, otherwise {@code false}
     */
    @Override
    public boolean needFood() {
        return this.player.getFoodStats().needFood();
    }

    /**
     * Whether the player is in a three dimensional room to render
     *
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @return {@code true} if the player is in the three dimensional room, otherwise {@code false}
     */
    @Override
    public boolean isRangeToRender3d(double x, double y, double z) {
        return this.player.isInRangeToRender3d(x, y, z);
    }

    /**
     * Whether the player is in the range to render.
     *
     * @param distance The distance
     * @return {@code true} if the player is in range, otherwise {@code false}
     */
    @Override
    public boolean isInRangeToRender(double distance) {
        return this.player.isInRangeToRenderDist(distance);
    }

    /**
     * Whether the player is alive
     *
     * @return {@code true} if the player is alive, otherwise {@code false}
     */
    @Override
    public boolean isAlive() {
        return this.player.isAlive();
    }

    /**
     * Whether the player is sprinting
     *
     * @return {@code true} if the player is sprinting, otherwise {@code false}
     */
    @Override
    public boolean isSprinting() {
        return this.player.isSprinting();
    }

    /**
     * Whether the player is on a ladder
     *
     * @return {@code true} if the player is on ladder, otherwise {@code false}
     */
    @Override
    public boolean isOnLadder() {
        return this.player.isOnLadder();
    }

    /**
     * Whether the player is sleeping
     *
     * @return {@code true} if the player is sleeping, otherwise {@code false}
     */
    @Override
    public boolean isSleeping() {
        return this.player.isSleeping();
    }

    /**
     * Whether the player is swimming
     *
     * @return {@code true} if the player is swimming, otherwise {@code false}
     */
    @Override
    public boolean isSwimming() {
        return this.player.isSwimming();
    }

    /**
     * Whether the player is wet
     *
     * @return {@code true} if the player is wet, otherwise {@code false}
     */
    @Override
    public boolean isWet() {
        return this.player.isWet();
    }

    /**
     * Whether the player is crouched
     *
     * @return {@code true} if the player is crouched, otherwise {@code false}
     */
    @Override
    public boolean isCrouching() {
        return this.player.isCrouching();
    }

    /**
     * Whether the player glows
     *
     * @return {@code true} if the player glows, otherwise {@code false}
     */
    @Override
    public boolean isGlowing() {
        return this.player.isGlowing();
    }

    /**
     * Whether the player is sneaked
     *
     * @return {@code true} if the player is sneaked, otherwise {@code false}
     */
    @Override
    public boolean isSneaking() {
        return this.player.isSneaking();
    }

    /**
     * Whether the player is in water
     *
     * @return {@code true} if the player is in water, otherwise {@code false}
     */
    @Override
    public boolean isInWater() {
        return this.player.isInWater();
    }

    /**
     * Whether the player is in lava
     *
     * @return {@code true} if the player is in lava, otherwise {@code false}
     */
    @Override
    public boolean isInLava() {
        return this.player.isInLava();
    }

    /**
     * Whether the player is wearing the given clothing.
     *
     * @param playerClothing The clothing that should be worn.
     * @return {@code true} if the player is wearing the clothing, otherwise {@code false}
     */
    @Override
    public boolean isWearing(PlayerClothing playerClothing) {
        return this.player.isWearing(this.playerClothingSerializer.serialize(playerClothing));
    }

    /**
     * Whether the player is invisible
     *
     * @return {@code true} if the player is invisible, otherwise {@code false}
     */
    @Override
    public boolean isInvisible() {
        return this.player.isInvisible();
    }

    /**
     * Sets the player invisible
     *
     * @param invisible The new state for invisible
     */
    @Override
    public void setInvisible(boolean invisible) {
        this.player.setInvisible(invisible);
    }

    /**
     * Whether the player is burning
     *
     * @return {@code true} if the player is burning, otherwise {@code false}
     */
    @Override
    public boolean isBurning() {
        return this.player.isBurning();
    }

    /**
     * Whether the player is flying with an elytra.
     *
     * @return {@code true} if the player is flying with an elytra, otherwise {@code false}
     */
    @Override
    public boolean isElytraFlying() {
        return this.player.isElytraFlying();
    }

    /**
     * Retrieves the absorption amount of this player.
     *
     * @return the absorption amount of this player.
     */
    @Override
    public float getAbsorptionAmount() {
        return this.player.getAbsorptionAmount();
    }

    /**
     * Retrieves the total armor value of this player.
     *
     * @return the total armor value of this player.
     */
    @Override
    public int getTotalArmorValue() {
        return this.player.getTotalArmorValue();
    }

    /**
     * Prints a message in this player chat
     *
     * @param component The message to print
     */
    @Override
    public void sendMessage(Object component) {

    }

    /**
     * Retrieves the network player info of this player.
     *
     * @return the network player info of this  player
     */
    @Override
    public NetworkPlayerInfo getNetworkPlayerInfo() {
        return InjectionHolder.getInjectedInstance(NetworkPlayerInfo.class);
    }

    /**
     * Plays a sound to the player.
     *
     * @param sound  The sound to play.
     * @param volume The volume of this sound.
     * @param pitch  The pitch of this sound.
     */
    @Override
    public void playSound(Sound sound, float volume, float pitch) {
        this.playSound(sound, this.getSoundCategory(), volume, pitch);
    }

    /**
     * Plays a sound to the player.
     *
     * @param sound    The sound to play.
     * @param category The category for this sound.
     * @param volume   The volume of this sound.
     * @param pitch    The pitch of this sound.
     */
    @Override
    public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
        this.player.playSound(
                this.soundSerializer.serialize(sound),
                this.soundCategorySerializer.serialize(category),
                volume,
                pitch
        );
    }

    /**
     * Retrieves the fall distance of this player.
     *
     * @return the fall distance of this player.
     */
    @Override
    public float getFallDistance() {
        return this.player.fallDistance;
    }

    /**
     * Retrieves the maximal fall distance of this player.
     *
     * @return the maximal fall distance of this player.
     */
    @Override
    public int getMaxFallDistance() {
        return this.player.getMaxFallHeight();
    }

    /**
     * Swings the arm through the given hand.
     *
     * @param hand The hand to swing.
     */
    @Override
    public void swingArm(Hand hand) {
        this.player.swingArm(this.handSerializer.serialize(hand));
    }

    /**
     * Retrieves the maximal in portal time of this player.
     *
     * @return the maximal in portal time of this player.
     */
    @Override
    public int getMaxInPortalTime() {
        return this.player.getMaxInPortalTime();
    }

    /**
     * Retrieves the fly speed of this player.
     *
     * @return the fly speed of this player.
     */
    @Override
    public float getFlySpeed() {
        return this.player.abilities.getFlySpeed();
    }

    /**
     * Sets the fly speed of this player.
     *
     * @param speed The new fly speed.
     */
    @Override
    public void setFlySpeed(float speed) {
        this.player.abilities.setFlySpeed(speed);
    }

    /**
     * Retrieves the walk speed of this player.
     *
     * @return the walk speed of this player.
     */
    @Override
    public float getWalkSpeed() {
        return this.player.abilities.getWalkSpeed();
    }

    /**
     * Sets the walk speed of this player.
     *
     * @param speed The new walk speed.
     */
    @Override
    public void setWalkSpeed(float speed) {
        this.player.abilities.setWalkSpeed(speed);
    }

    /**
     * Whether the player is on the ground.
     *
     * @return {@code true} if the player is on the ground, otherwise {@code false}
     */
    @Override
    public boolean isOnGround() {
        return this.player.onGround;
    }

    /**
     * Retrieves the luck of this player.
     *
     * @return the luck of this player.
     */
    @Override
    public float getLuck() {
        return this.player.getLuck();
    }

    /**
     * Retrieves the primary hand this player.
     *
     * @return the primary hand this player.
     */
    @Override
    public Hand.Side getPrimaryHand() {
        return this.handSideSerializer.deserialize(this.player.getPrimaryHand());
    }

    /**
     * Sets the primary hand of this player.
     *
     * @param side the primary hand of this player.
     */
    @Override
    public void setPrimaryHand(Hand.Side side) {
        this.player.setPrimaryHand(this.handSideSerializer.serialize(side));
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
        return this.getNetworkPlayerInfo().getSkinLocation();
    }

    /**
     * Retrieves the location of the player's cloak
     *
     * @return the cloak location
     */
    @Override
    public ResourceLocation getCloakLocation() {
        return this.getNetworkPlayerInfo().getCloakLocation();
    }

    /**
     * Retrieves the location of the player's elytra
     *
     * @return the elytra location
     */
    @Override
    public ResourceLocation getElytraLocation() {
        return this.getNetworkPlayerInfo().getElytraLocation();
    }

    /**
     * Whether the player has a skin.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasSkin() {
        NetworkPlayerInfo playerInfo = this.getNetworkPlayerInfo();
        return playerInfo != null && playerInfo.hasElytra();
    }

    /**
     * Whether the player has a cloak.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasCloak() {
        NetworkPlayerInfo playerInfo = this.getNetworkPlayerInfo();
        return playerInfo != null && playerInfo.hasElytra();
    }

    /**
     * Whether the player has a elytra.
     *
     * @return {@code true} if this player has a skin, otherwise {@code false}
     */
    @Override
    public boolean hasElytra() {
        NetworkPlayerInfo playerInfo = this.getNetworkPlayerInfo();
        return playerInfo != null && playerInfo.hasElytra();
    }

}
