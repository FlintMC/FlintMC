package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.*;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

import java.util.UUID;

/**
 * 1.15.2 implementation of {@link Player}
 */
@Implement(value = Player.class, version = "1.15.2")
public class VersionedPlayer implements Player<AbstractClientPlayerEntity> {

    private AbstractClientPlayerEntity player;

    protected final HandSerializer<net.minecraft.util.Hand> handSerializer;
    protected final HandSideSerializer<HandSide> handSideSerializer;
    protected final GameModeSerializer<GameType> gameModeSerializer;
    protected final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
    protected final MinecraftComponentMapper minecraftComponentMapper;
    protected final PlayerClothingSerializer<PlayerModelPart> playerClothingSerializer;
    protected final PoseSerializer<Pose> poseSerializer;
    protected final SoundCategorySerializer<net.minecraft.util.SoundCategory> soundCategorySerializer;
    protected final SoundSerializer<SoundEvent> soundSerializer;

    @Inject
    protected VersionedPlayer(
            HandSerializer handSerializer,
            HandSideSerializer handSideSerializer,
            GameModeSerializer gameModeSerializer,
            GameProfileSerializer gameProfileSerializer,
            MinecraftComponentMapper minecraftComponentMapper,
            PlayerClothingSerializer playerClothingSerializer,
            PoseSerializer poseSerializer,
            SoundCategorySerializer soundCategorySerializer,
            SoundSerializer soundSerializer
    ) {
        this.handSerializer = handSerializer;
        this.handSideSerializer = handSideSerializer;
        this.gameModeSerializer = gameModeSerializer;
        this.gameProfileSerializer = gameProfileSerializer;
        this.minecraftComponentMapper = minecraftComponentMapper;
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
     * Retrieves the player.
     *
     * @return The player.
     */
    @Override
    public AbstractClientPlayerEntity getPlayer() {
        return this.player;
    }

    /**
     * Retrieves the world of this player.
     *
     * @return The world of this player.
     */
    @Override
    public ClientWorld getWorld() {
        return InjectionHolder.getInjectedInstance(ClientWorld.class);
    }

    /**
     * Retrieves the game profile of this player.
     *
     * @return The game profile of this player.
     */
    @Override
    public GameProfile getGameProfile() {
        return this.gameProfileSerializer.deserialize(this.player.getGameProfile());
    }

    /**
     * Retrieves the name of this player.
     *
     * @return The name of this player
     */
    @Override
    public ChatComponent getName() {
        return this.minecraftComponentMapper.fromMinecraft(this.player.getName());
    }

    /**
     * Retrieves the display name of this player
     *
     * @return The display name of this player
     */
    @Override
    public ChatComponent getDisplayName() {
        return this.minecraftComponentMapper.fromMinecraft(this.player.getDisplayName());
    }

    /**
     * Retrieves the display name and the unique identifier of this player.
     *
     * @return The display name and the unique identiifer of this player.
     */
    @Override
    public ChatComponent getDisplayNameAndUniqueId() {
        return this.minecraftComponentMapper.fromMinecraft(this.player.getDisplayNameAndUUID());
    }

    /**
     * Retrieves the unique identifier of this player.
     *
     * @return The unique identifier of this player
     */
    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueID();
    }

    /**
     * Retrieves the health of this player.
     *
     * @return The health of this player
     */
    @Override
    public float getHealth() {
        return this.player.getHealth();
    }

    /**
     * Retrieves the list name of this player.
     *
     * @return The list name of this player.
     */
    @Override
    public String getPlayerListName() {
        return this.player.getScoreboardName();
    }

    /**
     * Retrieves the world time.
     *
     * @return The world time.
     */
    @Override
    public long getPlayerTime() {
        return this.getWorld().getTime();
    }

    /**
     * Retrieves the x position of this player.
     *
     * @return The x position of this player
     */
    @Override
    public double getX() {
        return this.player.getPosX();
    }

    /**
     * Retrieves the y position of this player.
     *
     * @return The y position of this player
     */
    @Override
    public double getY() {
        return this.player.getPosY();
    }

    /**
     * Retrieves the z position of this player.
     *
     * @return The z position of this playe
     */
    @Override
    public double getZ() {
        return this.player.getPosZ();
    }

    /**
     * Retrieves the pitch of this player.
     *
     * @return The pitch of this player.
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
     * @return The pitch of this player.
     */
    @Override
    public float getPitch(float partialTicks) {
        return this.player.getPitch(partialTicks);
    }

    /**
     * Retrieves the yaw of this player.
     *
     * @return The yaw of this player.
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
     * @return The yaw of this player.
     */
    @Override
    public float getYaw(float partialTicks) {
        return this.player.getYaw(partialTicks);
    }

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return The rotation yaw head of this player.
     */
    @Override
    public float getRotationYawHead() {
        return this.player.getRotationYawHead();
    }

    /**
     * Retrieves the eye height of this player.
     *
     * @return The eye height of this player.
     */
    @Override
    public float getEyeHeight() {
        return this.player.getEyeHeight();
    }

    /**
     * Retrieves the eye height of this player.
     *
     * @param entityPose The current pose for the eye height
     * @return The eye height of this player
     */
    @Override
    public float getEyeHeight(EntityPose entityPose) {
        return this.player.getEyeHeight(this.poseSerializer.serialize(entityPose));
    }

    /**
     * Retrieves the y position of the eyes of this player.
     *
     * @return The y position of the eyes of this player
     */
    @Override
    public double getPosYEye() {
        return this.player.getPosYEye();
    }

    /**
     * Retrieves the pose of this player.
     *
     * @return The pose of this player.
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
     * @return The active hand of this player
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
     * @return The active item stack of this player
     */
    // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready 
    @Override
    public Object getActiveItemStack() {
        return this.player.getActiveItemStack();
    }

    /**
     * Retrieves use count of the item.
     *
     * @return The use count of the item
     */
    @Override
    public int getItemInUseCount() {
        return this.player.getItemInUseCount();
    }

    /**
     * Retrieves the maximal use count of the item.
     *
     * @return The maximal use count of this item.
     */
    @Override
    public int getItemInUseMaxCount() {
        return this.player.getItemInUseMaxCount();
    }

    /**
     * Retrieves the food level of this player.
     *
     * @return The food level of this player.
     */
    @Override
    public int getFoodLevel() {
        return this.player.getFoodStats().getFoodLevel();
    }

    /**
     * Retrieves the saturation of this player.
     *
     * @return The saturation of this player.
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
     * Sets the absorption amount of this player.
     *
     * @param amount The new absorption amount.
     */
    @Override
    public void setAbsorptionAmount(float amount) {
        this.player.setAbsorptionAmount(amount);
    }

    /**
     * Retrieves the absorption amount of this player.
     *
     * @return Theabsorption amount of this player.
     */
    @Override
    public float getAbsorptionAmount() {
        return this.player.getAbsorptionAmount();
    }

    /**
     * Retrieves the total armor value of this player.
     *
     * @return The total armor value of this player.
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
    public void sendMessage(ChatComponent component) {
        this.player.sendMessage((ITextComponent) this.minecraftComponentMapper.toMinecraft(component));
    }

    /**
     * Retrieves the network player info of this player.
     *
     * @return The network player info of this  player
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
     * @return The fall distance of this player.
     */
    @Override
    public float getFallDistance() {
        return this.player.fallDistance;
    }

    /**
     * Retrieves the maximal fall distance of this player.
     *
     * @return The maximal fall distance of this player.
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
     * @return The maximal in portal time of this player.
     */
    @Override
    public int getMaxInPortalTime() {
        return this.player.getMaxInPortalTime();
    }

    /**
     * Retrieves the fly speed of this player.
     *
     * @return The fly speed of this player.
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
     * @return The walk speed of this player.
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
     * @return The luck of this player.
     */
    @Override
    public float getLuck() {
        return this.player.getLuck();
    }

    /**
     * Retrieves the primary hand this player.
     *
     * @return The primary hand this player.
     */
    @Override
    public Hand.Side getPrimaryHand() {
        return this.handSideSerializer.deserialize(this.player.getPrimaryHand());
    }

    /**
     * Sets the primary hand of this player.
     *
     * @param side The new primary hand of this player.
     */
    @Override
    public void setPrimaryHand(Hand.Side side) {
        this.player.setPrimaryHand(this.handSideSerializer.serialize(side));
    }

    /**
     * Whether the player can use command block.
     *
     * @return {@code true} if the player can use command blocks, otherwise {@code false}
     */
    @Override
    public boolean canUseCommandBlock() {
        return this.player.canUseCommandBlock();
    }

    /**
     * Retrieves the cooldown period of this player.
     *
     * @return The cooldown period of this player.
     */
    @Override
    public float getCooldownPeriod() {
        return this.player.getCooldownPeriod();
    }

    /**
     * Retrieves the cooled attack strength of this player.
     *
     * @param adjustTicks The ticks to adjust the cooled strength of the attack.
     * @return The cooled attack strength of this player.
     */
    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return this.player.getCooledAttackStrength(adjustTicks);
    }

    /**
     * Resets the cooldown of this player.
     */
    @Override
    public void resetCooldown() {
        this.player.resetCooldown();
    }

    /**
     * Whether the player has reduced debug.
     *
     * @return {@code true} if the player has reduced debug, otherwise {@code false}
     */
    @Override
    public boolean hasReducedDebug() {
        return this.player.hasReducedDebug();
    }

    /**
     * Sets the reduced debug for this player.
     *
     * @param reducedDebug The new reduced debug.
     */
    @Override
    public void setReducedDebug(boolean reducedDebug) {
        this.player.setReducedDebug(reducedDebug);
    }

    /**
     * Whether the item stack at the slot can be replaced.
     *
     * @param slot      The slot that should be replaced.
     * @param itemStack The item stack to be replaced.
     * @return {@code true} if the item stack can be replaced, otherwise {@code false}
     */
    @Override
    public boolean replaceItemInInventory(int slot, Object itemStack) {
        return this.player.replaceItemInInventory(slot, (ItemStack) itemStack);
    }

    /**
     * Whether the player is pushed by water.
     *
     * @return {@code true} if the player pushed by water, otherwise {@code false}
     */
    @Override
    public boolean isPushedByWater() {
        return this.player.isPushedByWater();
    }

    /**
     * Retrieves an iterable collection of the equipment held by that player.
     *
     * @return An iterable collection of the equipment held by that player.
     */
    @Override
    public Iterable<Object> getHeldEquipment() {
        /*return this.player.getHeldEquipment();*/
        return null;
    }

    /**
     * Retrieves an iterable inventory of that player's armor.
     *
     * @return An iterable inventory of that player's armor.
     */
    @Override
    public Iterable<Object> getArmorInventoryList() {
        /*return this.player.getArmorInventoryList();*/
        return null;
    }

    /**
     * Whether the item stack was added to this main inventory.
     *
     * @param itemStack The item stack to be added
     * @return {@code true} if was the item stack added, otherwise {@code false}
     */
    @Override
    public boolean addItemStackToInventory(Object itemStack) {
        return this.player.addItemStackToInventory((ItemStack) itemStack);
    }

    /**
     * Whether the player is allowed to edit.
     *
     * @return {@code true} if the player is allowed to edit, otherwise {@code false}
     */
    @Override
    public boolean isAllowEdit() {
        return this.player.isAllowEdit();
    }

    /**
     * Whether the player should heal.
     *
     * @return {@code true} if the player should heal, otherwise {@code false}
     */
    @Override
    public boolean shouldHeal() {
        return this.player.shouldHeal();
    }

    /**
     * Whether the player can eat.
     *
     * @param ignoreHunger Whether hunger should be ignored.
     * @return {@code true} if the player can eat, otherwise {@code false}
     */
    @Override
    public boolean canEat(boolean ignoreHunger) {
        return this.player.canEat(ignoreHunger);
    }

    /**
     * Adds the exhaustion of this player.
     *
     * @param exhaustion The exhaustion to be added.
     */
    @Override
    public void addExhaustion(float exhaustion) {
        this.player.addExhaustion(exhaustion);
    }

    /**
     * Adds the experience level to this player.
     *
     * @param levels The levels to be added.
     */
    @Override
    public void addExperienceLevel(int levels) {
        this.player.addExperienceLevel(levels);
    }

    /**
     * Retrieves the experience bar cap of this player.
     *
     * @return The experience bar cap of this player.
     */
    @Override
    public int experienceBarCap() {
        return this.player.xpBarCap();
    }

    /**
     * Retrieves the experience speed of this player.
     *
     * @return The experience speed of this player.
     */
    @Override
    public int getExperienceSpeed() {
        return this.player.getXPSeed();
    }

    /**
     * Gives this player experience points.
     *
     * @param points The points to be assigned
     */
    @Override
    public void giveExperiencePoints(int points) {
        this.player.giveExperiencePoints(points);
    }

    /**
     * Whether can be tried to start the fall flying of this player.
     *
     * @return {@code true} if can be tried to start the fall flying, otherwise {@code false}
     */
    @Override
    public boolean tryToStartFallFlying() {
        return this.player.tryToStartFallFlying();
    }

    /**
     * Starts the fall flying of this player.
     */
    @Override
    public void startFallFlying() {
        this.player.startFallFlying();
    }

    /**
     * Stops the fall flying of this player.
     */
    @Override
    public void stopFallFlying() {
        this.player.stopFallFlying();
    }

    /**
     * Lets the player jump.
     */
    @Override
    public void jump() {
        this.player.jump();
    }

    /**
     * Updates the swimming of this player.
     */
    @Override
    public void updateSwimming() {
        this.player.updateSwimming();
    }

    /**
     * Retrieves the AI move speed of this player.
     *
     * @return The AI move speed of this player.
     */
    @Override
    public float getAIMoveSpeed() {
        return this.player.getAIMoveSpeed();
    }

    /**
     * Adds movement stats to this player.
     *
     * @param x The x position to be added
     * @param y The y position to be added
     * @param z The z position to be added
     */
    @Override
    public void addMovementStat(double x, double y, double z) {
        this.player.addMovementStat(x, y, z);
    }

    /**
     * Whether the spawn is forced.
     *
     * @return {@code true} if the spawn is forced, otherwise {@code false}
     */
    @Override
    public boolean isSpawnForced() {
        return this.player.isSpawnForced();
    }

    /**
     * Whether the player is fully asleep.
     *
     * @return {@code true} if the player is fully  asleep, otherwise {@code false}
     */
    @Override
    public boolean isPlayerFullyAsleep() {
        return this.player.isPlayerFullyAsleep();
    }

    /**
     * Retrieves the sleep timer of this player.
     *
     * @return The sleep timer of this player.
     */
    @Override
    public int getSleepTimer() {
        return this.player.getSleepTimer();
    }

    /**
     * Wakes up this player.
     */
    @Override
    public void wakeUp() {
        this.player.wakeUp();
    }

    /**
     * Wakes up this player or updates all sleeping players.
     *
     * @param updateTimer           Updates the sleep timer
     * @param updateSleepingPlayers Updates all sleeping players.
     */
    @Override
    public void wakeUp(boolean updateTimer, boolean updateSleepingPlayers) {
        this.player.stopSleepInBed(updateTimer, updateSleepingPlayers);
    }

    /**
     * Disables the shield of this player.
     *
     * @param sprinting Whether the player is sprinting.
     */
    @Override
    public void disableShield(boolean sprinting) {
        this.player.disableShield(sprinting);
    }

    /**
     * Stops the ride of this player.
     */
    @Override
    public void stopRiding() {
        this.player.stopRiding();
    }

    /**
     * Whether the player can attack another player.
     *
     * @param player The player to be attacked
     * @return {@code true} if can the player be attacked, otherwise {@code false}
     */
    @Override
    public boolean canAttackPlayer(Player<AbstractClientPlayerEntity> player) {
        return this.player.canAttackPlayer(player.getPlayer());
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
     * @return The skin location
     */
    @Override
    public ResourceLocation getSkinLocation() {
        return this.getNetworkPlayerInfo().getSkinLocation();
    }

    /**
     * Retrieves the location of the player's cloak
     *
     * @return The cloak location
     */
    @Override
    public ResourceLocation getCloakLocation() {
        return this.getNetworkPlayerInfo().getCloakLocation();
    }

    /**
     * Retrieves the location of the player's elytra
     *
     * @return The elytra location
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

    /**
     * Whether the item has a cooldown.
     *
     * @param item The item to be checked
     * @return {@code true} if the item has a cooldown, otherwise {@code false}
     */
    @Override
    public boolean hasCooldown(Object item) {
        return this.player.getCooldownTracker().hasCooldown((Item) item);
    }

    /**
     * Retrieves the cooldown of the given item.
     *
     * @param item         The item to get the cooldown
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick.
     * @return The cooldown of this given item.
     */
    @Override
    public float getCooldown(Object item, float partialTicks) {
        return this.player.getCooldownTracker().getCooldown((Item) item, partialTicks);
    }

    /**
     * Sets the for the cooldown tracking.
     *
     * @param item  The item for setting the cooldown.
     * @param ticks The ticks, how long the cooldown lasts.
     */
    @Override
    public void setCooldown(Object item, int ticks) {
        this.player.getCooldownTracker().setCooldown((Item) item, ticks);
    }

    /**
     * Retrieves the score of this player.
     *
     * @return The score of this player.
     */
    @Override
    public int getScore() {
        return this.player.getScore();
    }

    /**
     * Sets the score of this player.
     *
     * @param score The new score
     */
    @Override
    public void setScore(int score) {
        this.player.setScore(score);
    }

    /**
     * Adds the score to this player.
     *
     * @param score The score to be added
     */
    @Override
    public void addScore(int score) {
        this.player.addScore(score);
    }

    /**
     * Whether the selected item can be dropped.
     *
     * @param dropEntireStack Whether the entire stack can be dropped.
     * @return {@code true} if the selected item can be dropped, otherwise {@code false}
     */
    @Override
    public boolean drop(boolean dropEntireStack) {
        return this.player.drop(dropEntireStack);
    }

    /**
     * Retrieves the dropped item as an entity.
     *
     * @param droppedItem The dropped item
     * @param traceItem   Whether the item can be traced.
     * @return The dropped item as an entity, or {@code null}
     */
    @Override
    public Object dropItem(Object droppedItem, boolean traceItem) {
        return this.player.dropItem((ItemStack) droppedItem, traceItem);
    }

    /**
     * Retrieves the dropped item as an entity.
     *
     * @param droppedItem The dropped item
     * @param dropAround  If {@code true}, the item will be thrown in a random direction
     *                    from the entity regardless of which direction the entity is facing
     * @param traceItem   Whether the item can be traced.
     * @return The dropped item as an entity, or {@code null}
     */
    @Override
    public Object dropItem(Object droppedItem, boolean dropAround, boolean traceItem) {
        return this.player.dropItem((ItemStack) droppedItem, dropAround, traceItem);
    }

    /**
     * Retrieves the digging speed of the given block state for this player.
     *
     * @param blockState The block state that is to receive the dig speed.
     * @return The digging speed of the block state for this player.
     */
    @Override
    public float getDigSpeed(Object blockState) {
        return this.player.getDigSpeed((BlockState) blockState);
    }

    /**
     * Whether the player can harvest the block.
     *
     * @param blockState The block to be harvested
     * @return {@code true} if the player can harvest the block, otherwise {@code false}.
     */
    @Override
    public boolean canHarvestBlock(Object blockState) {
        return this.player.canHarvestBlock((BlockState) blockState);
    }

    /**
     * Reads the additional of the given compound nbt.
     *
     * @param compoundNBT The compound nbt to be read.
     */
    @Override
    public void readAdditional(Object compoundNBT) {
        this.player.readAdditional((CompoundNBT) compoundNBT);
    }

    /**
     * Writes into the additional of this player.
     *
     * @param compoundNBT The additional to be written.
     */
    @Override
    public void writeAdditional(Object compoundNBT) {
        this.player.writeAdditional((CompoundNBT) compoundNBT);
    }

    /**
     * Sends a status message to this player.
     *
     * @param component The message for this status.
     * @param actionBar Whether to send to the action bar.
     */
    @Override
    public void sendStatusMessage(ChatComponent component, boolean actionBar) {
        this.player.sendStatusMessage((ITextComponent) this.minecraftComponentMapper.toMinecraft(component), actionBar);
    }

    /**
     * Finds shootable items in the inventory of this player.
     *
     * @param shootable The item to be fired.
     * @return An item to be fired or an empty item.
     */
    @Override
    public Object findAmmo(Object shootable) {
        return this.player.findAmmo((ItemStack) shootable);
    }

    /**
     * Whether the player can pick up the item.
     *
     * @param itemStack The item to be pick up
     * @return {@code true} if the player can pick up the item, otherwise {@code false}
     */
    @Override
    public boolean canPickUpItem(Object itemStack) {
        return this.player.canPickUpItem((ItemStack) itemStack);
    }

    /**
     * Adds should entity to this player.
     *
     * @param compoundNbt The entity as a compound nbt
     * @return {@code true} if an entity was added to the shoulder, otherwise {@code false}
     */
    @Override
    public boolean addShoulderEntity(Object compoundNbt) {
        return this.player.addShoulderEntity((CompoundNBT) compoundNbt);
    }

    /**
     * Retrieves the entity which is on the left shoulder.
     *
     * @return The entity as a compound nbt.
     */
    @Override
    public Object getLeftShoulderEntity() {
        return this.player.getLeftShoulderEntity();
    }

    /**
     * Retrieves the entity which is on the right shoulder.
     *
     * @return The entity as a compound nbt.
     */
    @Override
    public Object getRightShoulderEntity() {
        return this.player.getRightShoulderEntity();
    }

    /**
     * Retrieves the fire timer of this player.
     *
     * @return The fire timer of this player.
     */
    @Override
    public int getFireTimer() {
        return this.player.getFireTimer();
    }

    /**
     * Retrieves the step height of this player.
     *
     * @return The step height of this player.
     */
    @Override
    public float getStepHeight() {
        return this.player.stepHeight;
    }

    /**
     * Retrieves the x rotate elytra of this player.
     *
     * @return The x rotate elytra of this player.
     */
    @Override
    public float getRotateElytraX() {
        return this.player.rotateElytraX;
    }

    /**
     * Retrieves the y rotate elytra of this player.
     *
     * @return The y rotate elytra of this player.
     */
    @Override
    public float getRotateElytraY() {
        return this.player.rotateElytraY;
    }

    /**
     * Retrieves the z rotate elytra of this player.
     *
     * @return The z rotate elytra of this player.
     */
    @Override
    public float getRotateElytraZ() {
        return this.player.rotateElytraZ;
    }

    /**
     * Whether the player is collided.
     *
     * @return {@code true} if the player is collided, otherwise {@code false}
     */
    @Override
    public boolean isCollided() {
        return this.player.collided;
    }

    /**
     * Whether the player is collided horizontally.
     *
     * @return {@code true} if the player is collided horizontally, otherwise {@code false}
     */
    @Override
    public boolean isCollidedHorizontally() {
        return this.player.collidedHorizontally;
    }

    /**
     * Whether the player is collided vertically.
     *
     * @return {@code true} if the player is collided vertically, otherwise {@code false}
     */
    @Override
    public boolean isCollidedVertically() {
        return this.player.collidedVertically;
    }

    /**
     * Opens a sign editor.
     *
     * @param signTileEntity The sign to be edited.
     */
    @Override
    public void openSignEditor(Object signTileEntity) {
        this.player.openSignEditor((SignTileEntity) signTileEntity);
    }

    /**
     * Opens a minecart command block.
     *
     * @param commandBlock The minecart command block to be opened.
     */
    @Override
    public void openMinecartCommandBlock(Object commandBlock) {
        this.player.openMinecartCommandBlock((CommandBlockLogic) commandBlock);
    }

    /**
     * Opens a command block.
     *
     * @param commandBlock The command block to be opened.
     */
    @Override
    public void openCommandBlock(Object commandBlock) {
        this.player.openCommandBlock((CommandBlockTileEntity) commandBlock);
    }

    /**
     * Opens a structure block.
     *
     * @param structureBlock The structure block to be opened.
     */
    @Override
    public void openStructureBlock(Object structureBlock) {
        this.player.openStructureBlock((StructureBlockTileEntity) structureBlock);
    }

    /**
     * Opens a jigsaw.
     *
     * @param jigsaw The jigsaw to be opened.
     */
    @Override
    public void openJigsaw(Object jigsaw) {
        this.player.openJigsaw((JigsawTileEntity) jigsaw);
    }

    /**
     * Opens a horse inventory
     *
     * @param horse     The horse that has an inventory
     * @param inventory Inventory of the horse
     */
    @Override
    public void openHorseInventory(Object horse, Object inventory) {
        this.player.openHorseInventory((AbstractHorseEntity) horse, (IInventory) inventory);
    }

    /**
     * Opens a merchant inventory.
     *
     * @param merchantOffers  The offers of the merchant
     * @param container       The container identifier for this merchant
     * @param levelProgress   The level progress of this merchant.<br>
     *                        <b>Note:</b><br>
     *                        1 = Novice<br>
     *                        2 = Apprentice<br>
     *                        3 = Journeyman<br>
     *                        4 = Expert<br>
     *                        5 = Master
     * @param experience      The total experience for this villager (Always 0 for the wandering trader)
     * @param regularVillager {@code True} if this is a regular villager,
     *                        otherwise {@code false} for the wandering trader. When {@code false},
     *                        hides the villager level  and some other GUI elements
     * @param refreshable     {@code True} for regular villagers and {@code false} for the wandering trader.
     *                        If {@code true}, the "Villagers restock up to two times per day".
     */
    @Override
    public void openMerchantInventory(
            Object merchantOffers,
            int container,
            int levelProgress,
            int experience,
            boolean regularVillager,
            boolean refreshable
    ) {
        this.player.openMerchantContainer(
                container,
                (MerchantOffers) merchantOffers,
                levelProgress,
                experience,
                regularVillager,
                refreshable
        );
    }

    /**
     * Opens a book.
     *
     * @param itemStack The item stack which should be a book.
     * @param hand      The hand of this player.
     */
    @Override
    public void openBook(Object itemStack, Hand hand) {
        this.player.openBook((ItemStack) itemStack, this.handSerializer.serialize(hand));
    }

    /**
     * Prepare this player for spawning.
     */
    @Override
    public void preparePlayerToSpawn() {
        this.player.preparePlayerToSpawn();
    }

    /**
     * Whether block actions are restricted for this player.
     *
     * @param clientWorld    This world of this player
     * @param blockPos This position of this block
     * @param gameMode This game mode of this player
     * @return {@code true} if this player has restricted block actions, otherwise {@code false}
     */
    @Override
    public boolean blockActionRestricted(ClientWorld clientWorld, Object blockPos, GameMode gameMode) {
        return this.player.blockActionRestricted(
                (net.minecraft.world.World) this.getWorld().getClientWorld(),
                (BlockPos) blockPos,
                this.gameModeSerializer.serialize(gameMode)
        );
    }

    /**
     * Spawns sweep particles.
     */
    @Override
    public void spawnSweepParticles() {
        this.player.spawnSweepParticles();
    }

    /**
     * Respawns this player.
     */
    @Override
    public void respawnPlayer() {
        this.player.respawnPlayer();
    }

    /**
     * Sends the abilities of this player to the server.
     */
    @Override
    public void sendPlayerAbilities() {
        this.player.sendPlayerAbilities();
    }

    /**
     * Updates the game mode of this player.
     * <br>
     * <b>Note:</b> This is only on the server side.
     *
     * @param gameMode The new game of this player
     */
    @Override
    public void updateGameMode(GameMode gameMode) {
        this.player.setGameType(this.gameModeSerializer.serialize(gameMode));
    }

    /**
     * Enchants the given item stack with the cost.
     *
     * @param itemStack The item stack to enchant
     * @param cost      The cost of the enchant
     */
    @Override
    public void enchantItem(Object itemStack, int cost) {
        this.player.onEnchant((ItemStack) itemStack, cost);
    }

    /**
     * Retrieves the opened container of this player.
     *
     * @return The opened container of this player.
     */
    @Override
    public Object getOpenedContainer() {
        return this.player.openContainer;
    }

    /**
     * Retrieves the opened container of this player.
     *
     * @return The opened container of this player.
     */
    @Override
    public Object getPlayerContainer() {
        return this.player.container;
    }

    /**
     * Retrieves the previous camera yaw of this player.
     *
     * @return The previous camera yaw of this player.
     */
    @Override
    public float getPrevCameraYaw() {
        return this.player.prevCameraYaw;
    }

    /**
     * Retrieves the camera yaw of this player.
     *
     * @return The camera yaw of this player.
     */
    @Override
    public float getCameraYaw() {
        return this.player.cameraYaw;
    }

    /**
     * Retrieves the previous chasing position X-axis of this player.
     *
     * @return The previous chasing position X-axis  of this player.
     */
    @Override
    public double getPrevChasingPosX() {
        return this.player.prevChasingPosX;
    }

    /**
     * Retrieves the previous chasing position Y-axis of this player.
     *
     * @return The previous chasing position Y-axis  of this player.
     */
    @Override
    public double getPrevChasingPosY() {
        return this.player.prevChasingPosY;
    }

    /**
     * Retrieves the previous chasing position Z-axis of this player.
     *
     * @return The previous chasing position Z-axis  of this player.
     */
    @Override
    public double getPrevChasingPosZ() {
        return this.player.prevChasingPosZ;
    }

    /**
     * Retrieves the chasing position X-axis of this player.
     *
     * @return The chasing position X-axis  of this player.
     */
    @Override
    public double getChasingPosX() {
        return this.player.chasingPosX;
    }

    /**
     * Retrieves the chasing position Y-axis of this player.
     *
     * @return The chasing position Y-axis  of this player.
     */
    @Override
    public double getChasingPosY() {
        return this.player.chasingPosY;
    }

    /**
     * Retrieves the  chasing position Z-axis of this player.
     *
     * @return The chasing position Z-axis  of this player.
     */
    @Override
    public double getChasingPosZ() {
        return this.player.chasingPosZ;
    }

    /**
     * Removes the item from the cooldown tracking.
     *
     * @param item The item to be removed
     */
    @Override
    public void removeCooldown(Object item) {
        this.player.getCooldownTracker().removeCooldown((Item) item);
    }
}
