package net.flintmc.mcapi.internal.potion;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

@Implement(Potion.class)
public class DefaultPotion implements Potion {

  private final String name;
  private final List<StatusEffectInstance> effects;

  @AssistedInject
  public DefaultPotion(@Assisted("effects") StatusEffectInstance... effects) {
    this(null, effects);
  }

  @AssistedInject
  public DefaultPotion(
      @Assisted("name") @Nullable String name,
      @Assisted("effects") StatusEffectInstance... effects) {
    this.name = name;
    this.effects = ImmutableList.copyOf(effects);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public List<StatusEffectInstance> getStatusEffects() {
    return this.effects;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasInstantly() {
    return !this.effects.isEmpty()
        && this.effects.stream().anyMatch(effect -> effect.getPotion().isInstant());
  }
}
