package net.flintmc.mcapi.v1_15_2.render.image.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.render.image.util.ItemRenderer;
import net.flintmc.mcapi.resources.pack.ResourcePackReloadEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

@Singleton
@Implement(value = ItemRenderer.class, version = "1.15.2")
public class VersionedItemRenderer implements ItemRenderer {

  private final MinecraftItemMapper itemMapper;
  private final FontRenderer fontRenderer;
  private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
  private boolean initialized;

  @Inject
  private VersionedItemRenderer(MinecraftItemMapper itemMapper) {
    this.itemMapper = itemMapper;
    this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    this.fontRenderer = Minecraft.getInstance().fontRenderer;
  }

  @PostSubscribe
  public void handleResourceReload(ResourcePackReloadEvent event) {
    this.initialized = true;
  }

  @Override
  public void drawItemStack(float x, float y, ItemStack item) {
    this.drawRawItemStack(x, y, this.itemMapper.toMinecraft(item));
  }

  @Override
  public void drawRawItemStack(float x, float y, Object minecraftItem) {
    if (!this.initialized) {
      return;
    }

    if (!(minecraftItem instanceof net.minecraft.item.ItemStack)) {
      throw new IllegalArgumentException(
          "Provided object is not an " + net.minecraft.item.ItemStack.class);
    }
    net.minecraft.item.ItemStack item = (net.minecraft.item.ItemStack) minecraftItem;

    RenderSystem.disableDepthTest();
    RenderSystem.pushMatrix();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableRescaleNormal();
    RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    RenderSystem.translatef(0.0F, 0.0F, 32.0F);
    this.itemRenderer.zLevel = 200.0F;
    this.itemRenderer.renderItemAndEffectIntoGUI(item, (int) x, (int) y);
    this.itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, item, (int) x, (int) y, null);
    this.itemRenderer.zLevel = 0.0F;

    RenderSystem.popMatrix();
    RenderSystem.enableDepthTest();
  }
}
