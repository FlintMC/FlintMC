package net.flintmc.mcapi.v1_15_2.potion;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.EffectFoundation;

import java.util.List;

/**
 * 1.15.2 implementation of the {@link Potion}.
 */
@Implement(value = Potion.class, version = "1.15.2")
public class VersionedPotion implements Potion {

  private final String name;
  private final List<EffectFoundation> effects;

  @AssistedInject
  private VersionedPotion(@Assisted("effects") List effects) {
    this(null, effects);
  }

  @AssistedInject
  private VersionedPotion(@Assisted("name") String name, @Assisted("effects") List effects) {
    this.name = name;
    this.effects = effects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EffectFoundation> getEffects() {
    return this.effects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasInstantEffect() {
    if (!this.effects.isEmpty()) {

      for (EffectFoundation effect : this.effects) {
        if (effect.getPotion().isInstant()) {
          return true;
        }
      }

    }
    return false;
  }
}
