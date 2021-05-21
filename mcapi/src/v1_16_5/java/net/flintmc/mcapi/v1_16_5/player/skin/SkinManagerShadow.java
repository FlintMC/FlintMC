package net.flintmc.mcapi.v1_16_5.player.skin;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.flintmc.transform.shadow.MethodProxy;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

@Shadow("net.minecraft.client.resources.SkinManager")
public interface SkinManagerShadow {

  @MethodProxy
  ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type textureType, SkinManager.ISkinAvailableCallback skinAvailableCallback);

}
