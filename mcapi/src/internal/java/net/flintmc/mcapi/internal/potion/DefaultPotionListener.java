package net.flintmc.mcapi.internal.potion;

import com.google.inject.Singleton;
import java.util.Iterator;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.event.PotionAddEvent;
import net.flintmc.mcapi.potion.event.PotionRemoveEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent.State;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent;

@Singleton
public class DefaultPotionListener {

  @PreSubscribe
  public void potionAdd(PotionAddEvent event) {
    event.getLivingEntity().addPotionEffect(event.getStatusEffectInstance());
  }

  @PreSubscribe
  public void potionRemove(PotionRemoveEvent event) {
    event.getLivingEntity().removePotionEffect(event.getStatusEffect());
  }

  @PreSubscribe
  public void potionState(PotionStateUpdateEvent event) {
    if (event.getState() == State.FINISHED) {
      event.getLivingEntity().removePotionEffect(event.getStatusEffectInstance().getPotion());
    }
  }

  @PreSubscribe
  public void potionUpdate(PotionUpdateEvent event) {
    Iterator<StatusEffect> iterator =
        event.getLivingEntity().getActivePotions().keySet().iterator();

    while (iterator.hasNext()) {
      StatusEffect statusEffect = iterator.next();
      StatusEffectInstance statusEffectInstance =
          event.getLivingEntity().getActivePotions().get(statusEffect);

      if (!statusEffectInstance.update()) {
        iterator.remove();
      }
    }
  }
}
