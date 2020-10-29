package net.flintmc.mcapi.v1_15_2.potion.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.Effect;
import net.flintmc.mcapi.potion.effect.EffectFoundation;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.15.2 implementation of the {@link PotionMapper}.
 */
@Singleton
@Implement(value = PotionMapper.class, version = "1.15.2")
public class VersionedPotionMapper implements PotionMapper {

  private final Effect.Factory effectFactory;
  private final EffectFoundation.Factory effectFoundationFactory;
  private final Potion.Factory potionFactory;

  @Inject
  private VersionedPotionMapper(
          Effect.Factory effectFactory,
          EffectFoundation.Factory effectFoundationFactory,
          Potion.Factory potionFactory
  ) {
    this.effectFactory = effectFactory;
    this.effectFoundationFactory = effectFoundationFactory;
    this.potionFactory = potionFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftPotion(Potion potion) {
    return net.minecraft.potion.Potion.getPotionTypeForName(potion.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Potion fromMinecraftPotion(Object handle) {
    if (!(handle instanceof net.minecraft.potion.Potion)) {
      throw new IllegalArgumentException(
              handle.getClass().getName()
                      + " is not an instance of "
                      + net.minecraft.potion.Potion.class.getName());
    }

    net.minecraft.potion.Potion potion = (net.minecraft.potion.Potion) handle;

    List<EffectFoundation> effects = new ArrayList<>();
    /*for (EffectInstance effect : potion.getEffects()) {
      effects.add(this.fromMinecraftEffectInstance(effect));
    }*/

    String name = Registry.POTION.getKey(potion).getPath();

    return name == null ?
            this.potionFactory.create(
                    effects
            ) :
            this.potionFactory.create(
                    name,
                    effects
            );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffect(Effect effect) {
    return this.getEffect(effect.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Effect fromMinecraftEffect(Object handle) {
    if (!(handle instanceof net.minecraft.potion.Effect)) {
      throw new IllegalArgumentException(
              handle.getClass().getName()
                      + " is not an instance of "
                      + net.minecraft.potion.Effect.class.getName());
    }

    net.minecraft.potion.Effect effect = (net.minecraft.potion.Effect) handle;

    return this.effectFactory.create(
            effect,
            this.fromMinecraftEffectType(effect.getEffectType()),
            effect.getLiquidColor()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffectType(Effect.Type effectType) {
    switch (effectType) {
      case BENEFICIAL:
        return EffectType.BENEFICIAL;
      case HARMFUL:
        return EffectType.HARMFUL;
      default:
        return EffectType.NEUTRAL;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Effect.Type fromMinecraftEffectType(Object handle) {

    EffectType effectType = (EffectType) handle;

    switch (effectType) {
      case BENEFICIAL:
        return Effect.Type.BENEFICIAL;
      case HARMFUL:
        return Effect.Type.HARMFUL;
      default:
        return Effect.Type.NEUTRAL;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffectInstance(EffectFoundation foundation) {
    return null;/* new EffectInstance(
            (net.minecraft.potion.Effect) this.toMinecraftEffect(foundation.getPotion()),
            foundation.getDuration(),
            foundation.getAmplifier(),
            foundation.isAmbient(),
            foundation.doesShowParticles()
    );*/
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EffectFoundation fromMinecraftEffectInstance(Object handle) {
    /*if (!(handle instanceof EffectInstance)) {
      throw new IllegalArgumentException(
              handle.getClass().getName()
                      + " is not an instance of "
                      + EffectInstance.class.getName());
    }

    EffectInstance effectInstance = (EffectInstance) handle;

    return this.effectFoundationFactory.create(
            effectInstance,
            this.fromMinecraftEffect(effectInstance.getPotion()),
            effectInstance.getDuration(),
            effectInstance.getAmplifier(),
            effectInstance.isAmbient(),
            effectInstance.getIsPotionDurationMax(),
            effectInstance.doesShowParticles(),
            effectInstance.isShowIcon()
    );
     */
    return null;
  }

  private net.minecraft.potion.Effect getEffect(String name) {
    return Registry.EFFECTS.getOrDefault(ResourceLocation.tryCreate(name));
  }

}
