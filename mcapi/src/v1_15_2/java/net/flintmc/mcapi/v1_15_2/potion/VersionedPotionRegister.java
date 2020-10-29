package net.flintmc.mcapi.v1_15_2.potion;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.PotionRegister;
import net.flintmc.mcapi.potion.effect.Effect;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.registry.Registry;

import java.util.Map;

/**
 * 1.15.2 implementation of the {@link PotionRegister}.
 */
@Singleton
@Implement(value = PotionRegister.class, version = "1.15.2")
public class VersionedPotionRegister implements PotionRegister {

  private final Map<String, Effect> effects;
  private final Map<String, Potion> potions;

  private final PotionMapper potionMapper;

  @Inject
  private VersionedPotionRegister(PotionMapper potionMapper) {
    this.potionMapper = potionMapper;
    this.effects = Maps.newHashMap();
    this.potions = Maps.newHashMap();
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void convertEffectAndPotion() {
    for (net.minecraft.potion.Effect effect : Registry.EFFECTS) {
      this.effects.put(effect.getName(), this.potionMapper.fromMinecraftEffect(effect));
    }

    for (net.minecraft.potion.Potion potion : Registry.POTION) {
      this.potions.put(Registry.POTION.getKey(potion).getPath(), this.potionMapper.fromMinecraftPotion(potion));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Effect> getEffects() {
    return this.effects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Potion> getPotions() {
    return this.potions;
  }
}
