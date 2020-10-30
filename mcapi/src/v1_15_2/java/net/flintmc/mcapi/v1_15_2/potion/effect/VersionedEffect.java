package net.flintmc.mcapi.v1_15_2.potion.effect;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.potion.effect.Effect;

/**
 * 1.15.2 implementation of the {@link Effect}.
 */
@Implement(value = Effect.class, version = "1.15.2")
public class VersionedEffect implements Effect {

  private final net.minecraft.potion.Effect effect;
  private final Effect.Type effectType;
  private final int liquidColor;

  private final EntityMapper entityMapper;

  @AssistedInject
  private VersionedEffect(
          @Assisted("effect") Object effect,
          @Assisted("effectType") Type effectType,
          @Assisted("liquidColor") int liquidColor,
          EntityMapper entityMapper
  ) {
    this.effectType = effectType;
    this.liquidColor = liquidColor;
    this.entityMapper = entityMapper;

    if (!(effect instanceof net.minecraft.potion.Effect)) {
      throw new IllegalArgumentException(
              effect.getClass().getName()
                      + " is not an instance of "
                      + net.minecraft.potion.Effect.class.getName());
    }

    this.effect = (net.minecraft.potion.Effect) effect;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBeneficial() {
    return this.effectType == Effect.Type.BENEFICIAL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLiquidColor() {
    return this.liquidColor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getEffectType() {
    return this.effectType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.effect.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInstant() {
    return this.effect.isInstant();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReady(int duration, int amplifier) {
    return this.effect.isReady(duration, amplifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void affectEntity(Entity source, Entity attacker, LivingEntity livingEntity, int amplifier, double health) {
    this.effect.affectEntity(
            (net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(source),
            (net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(attacker),
            (net.minecraft.entity.LivingEntity) this.entityMapper.toMinecraftLivingEntity(livingEntity),
            amplifier,
            health
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    this.effect.performEffect(
            (net.minecraft.entity.LivingEntity) this.entityMapper.toMinecraftLivingEntity(livingEntity),
            amplifier
    );
  }
}
