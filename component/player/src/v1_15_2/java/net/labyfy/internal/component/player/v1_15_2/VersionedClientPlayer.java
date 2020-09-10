package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import io.netty.buffer.Unpooled;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.Hand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffers;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

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
            HandSideSerializer handSideSerializer,
            GameModeSerializer gameModeSerializer,
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
                gameModeSerializer,
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
     * @param player The new player
     */
    @Override
    public void setPlayer(AbstractClientPlayerEntity player) {
        super.setPlayer(player);
        this.clientPlayer = (ClientPlayerEntity) player;
    }

    @Override
    public AbstractClientPlayerEntity getPlayer() {
        return this.clientPlayer;
    }

    /**
     * Retrieves the inventory of this player.
     *
     * @return the inventory of this player
     */
    @Override
    public Object getPlayerInventory() {
        return this.clientPlayer.inventory;
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
     * Retrieves the server brand of this player.
     *
     * @return the server brand of this player.
     */
    @Override
    public String getServerBrand() {
        return this.clientPlayer.getServerBrand();
    }

    /**
     * Sets the server brand of this player.
     *
     * @param brand The new server brand.
     */
    @Override
    public void setServerBrand(String brand) {
        this.clientPlayer.setServerBrand(brand);
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
     * Sends a message to the chat
     *
     * @param content The message content
     */
    @Override
    public void sendMessage(String content) {
        this.clientPlayer.sendChatMessage(content);
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

    /**
     * Closes the screen and drop the item stack
     */
    @Override
    public void closeScreenAndDropStack() {
        this.clientPlayer.closeScreenAndDropStack();
    }

    /**
     * Whether the player can swim.
     *
     * @return {@code true} if the player can swim, otherwise {@code false}
     */
    @Override
    public boolean canSwim() {
        return this.clientPlayer.canSwim();
    }

    /**
     * Sends the horse inventory to the server.
     */
    @Override
    public void sendHorseInventory() {
        this.clientPlayer.sendHorseInventory();
    }

    /**
     * Retrieves the horse jump power.
     *
     * @return the horse jump power.
     */
    @Override
    public float getHorseJumpPower() {
        return this.clientPlayer.getHorseJumpPower();
    }

    /**
     * Sets the experience stats of this player.
     *
     * @param currentExperience The current experience of this player.
     * @param maxExperience     The max experience of this  player.
     * @param level             The level of this player.
     */
    @Override
    public void setExperienceStats(int currentExperience, int maxExperience, int level) {
        this.clientPlayer.setXPStats(currentExperience, maxExperience, level);
    }

    /**
     * Whether the player is holding the sneak key.
     *
     * @return {@code true} if the player is holding the sneak key, otherwise {@code false}
     */
    @Override
    public boolean isHoldingSneakKey() {
        return this.clientPlayer.func_228354_I_();
    }

    /**
     * Retrieves the statistics manager of this player.
     *
     * @return the statistics manager of this player.
     */
    @Override
    public Object getStatistics() {
        return this.clientPlayer.getStats();
    }

    /**
     * Retrieves the recipe book of this player.
     *
     * @return the recipe book of this player.
     */
    @Override
    public Object getRecipeBook() {
        return this.clientPlayer.getRecipeBook();
    }

    /**
     * Removes the highlight a recipe.
     *
     * @param recipe The recipe to removing the highlighting.
     */
    @Override
    public void removeRecipeHighlight(Object recipe) {
        this.clientPlayer.removeRecipeHighlight((IRecipe<?>) recipe);
    }

    /**
     * Updates the health of this player.
     * <br><br>
     * <b>Note:</b> This is only on the client side.
     *
     * @param health The new health of this player.
     */
    @Override
    public void updatePlayerHealth(float health) {
        this.clientPlayer.setPlayerSPHealth(health);
    }

    /**
     * Sends the abilities of this player to the server.
     */
    @Override
    public void sendPlayerAbilities() {
        this.clientPlayer.sendPlayerAbilities();
    }

    /**
     * Updates the entity action state of this player.
     */
    @Override
    public void updateEntityActionState() {
        this.clientPlayer.updateEntityActionState();
    }

    /**
     * Sends a plugin message to the server.
     *
     * @param channel The name of this channel
     * @param data    The data to be written into the channel
     */
    @Override
    public void sendPluginMessage(String channel, byte[] data) {
        this.clientPlayer.connection.sendPacket(
                new CCustomPayloadPacket(
                        new ResourceLocation(channel),
                        new PacketBuffer(Unpooled.wrappedBuffer(data))
                )
        );
    }

    /**
     * Retrieves the network communicator of this player.<br>
     * The network communicator allows this player to send packets to the server.
     *
     * @return the network communicator of this player.
     */
    @Override
    public Object getConnection() {
        return this.clientPlayer.connection;
    }

    /**
     * Sends a status message to this player.
     *
     * @param component The message for this status.
     * @param actionBar Whether to send to the action bar.
     */
    @Override
    public void sendStatusMessage(ChatComponent component, boolean actionBar) {
        this.clientPlayer.sendStatusMessage(
                (ITextComponent) this.minecraftComponentMapper.toMinecraft(component),
                actionBar
        );
    }

    /**
     * Opens a sign editor.
     *
     * @param signTileEntity The sign to be edited.
     */
    @Override
    public void openSignEditor(Object signTileEntity) {
        this.clientPlayer.openSignEditor((SignTileEntity) signTileEntity);
    }

    /**
     * Opens a minecart command block.
     *
     * @param commandBlock The minecart command block to be opened.
     */
    @Override
    public void openMinecartCommandBlock(Object commandBlock) {
        this.clientPlayer.openMinecartCommandBlock((CommandBlockLogic) commandBlock);
    }

    /**
     * Opens a command block.
     *
     * @param commandBlock The command block to be opened.
     */
    @Override
    public void openCommandBlock(Object commandBlock) {
        this.clientPlayer.openCommandBlock((CommandBlockTileEntity) commandBlock);
    }

    /**
     * Opens a structure block.
     *
     * @param structureBlock The structure block to be opened.
     */
    @Override
    public void openStructureBlock(Object structureBlock) {
        this.clientPlayer.openStructureBlock((StructureBlockTileEntity) structureBlock);
    }

    /**
     * Opens a jigsaw.
     *
     * @param jigsaw The jigsaw to be opened.
     */
    @Override
    public void openJigsaw(Object jigsaw) {
        this.clientPlayer.openJigsaw((JigsawTileEntity) jigsaw);
    }

    /**
     * Opens a book.
     *
     * @param itemStack The item stack which should be a book.
     * @param hand      The hand of this player.
     */
    @Override
    public void openBook(Object itemStack, Hand hand) {
        this.clientPlayer.openBook((ItemStack) itemStack, this.handSerializer.serialize(hand));
    }

    /**
     * Opens a horse inventory
     *
     * @param horse     The horse that has an inventory
     * @param inventory Inventory of the horse
     */
    @Override
    public void openHorseInventory(Object horse, Object inventory) {
        this.clientPlayer.openHorseInventory(
                (AbstractHorseEntity) horse,
                (IInventory) inventory
        );
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
        this.clientPlayer.openMerchantContainer(
                container,
                (MerchantOffers) merchantOffers,
                levelProgress,
                experience,
                regularVillager,
                refreshable
        );
    }

    /**
     * Retrieves the current biome name of this player.
     *
     * @return the current biome name or {@code null}
     */
    @Override
    public String getBiome() {
        World world = (World) this.getWorld();
        Entity renderViewEntity = Minecraft.getInstance().getRenderViewEntity();
        if(renderViewEntity == null) return null;
        BlockPos blockPos = new BlockPos(renderViewEntity);

        if(world != null) {
            String biomePath = Registry.BIOME.getKey(world.getBiome(blockPos)).getPath();
            String[] split = biomePath.split("_");
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < split.length; i++) {
                String biomeName = split[i];
                biomeName = biomeName.substring(0, 1).toUpperCase() + biomeName.substring(1).toLowerCase();
                if(i == split.length - 1) {
                    builder.append(biomeName);
                    break;
                }
                builder.append(biomeName).append(" ");
            }

            return builder.toString();
        }

        return null;
    }
}
