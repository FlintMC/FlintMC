package net.flintmc.mcapi.v1_15_2.entity.hook;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.transform.hook.Hook;
import net.minecraft.potion.EffectInstance;

@Singleton
public class VersionedLivingEntityHook {

  private final EntityMapper entityMapper;
  private final PotionMapper potionMapper;

  @Inject
  private VersionedLivingEntityHook(EntityMapper entityMapper, PotionMapper potionMapper) {
    this.entityMapper = entityMapper;
    this.potionMapper = potionMapper;
  }

  @Hook(
          className = "net.minecraft.entity.LivingEntity",
          methodName = "onNewPotionEffect",
          parameters = {
                  @Type(reference = EffectInstance.class)
          }
  )
  public void hookOnNewPotionEffect(@Named("instance") Object instance, @Named("args") Object[] args) {
    net.minecraft.entity.LivingEntity minecraftLivingEntity = (net.minecraft.entity.LivingEntity) instance;
    EffectInstance effectInstance = (EffectInstance) args[0];

    LivingEntity flintLivingEntity = this.entityMapper.fromMinecraftLivingEntity(minecraftLivingEntity);
    flintLivingEntity.getActivePotions().add(this.potionMapper.fromMinecraftEffectInstance(effectInstance));
  }

  @Hook(
          className = "net.minecraft.entity.LivingEntity",
          methodName = "onFinishedPotionEffect",
          parameters = {
                  @Type(reference = EffectInstance.class)
          }
  )
  public void hookOnFinishedPotionEffect(@Named("instance") Object instance, @Named("args") Object[] args) {
    net.minecraft.entity.LivingEntity minecraftLivingEntity = (net.minecraft.entity.LivingEntity) instance;
    EffectInstance effectInstance = (EffectInstance) args[0];

    LivingEntity flintLivingEntity = this.entityMapper.fromMinecraftLivingEntity(minecraftLivingEntity);
    flintLivingEntity.getActivePotions().removeIf(effectFoundation ->
            effectFoundation.getEffectName().equals(effectInstance.getEffectName()));
  }

}
