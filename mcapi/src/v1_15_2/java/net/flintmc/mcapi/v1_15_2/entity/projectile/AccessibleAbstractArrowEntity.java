package net.flintmc.mcapi.v1_15_2.entity.projectile;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.MethodProxy;
import net.flintmc.transform.shadow.Shadow;

/** A shadow interface for the abstract arrow entity. */
@Shadow("net.minecraft.entity.projectile.AbstractArrowEntity")
public interface AccessibleAbstractArrowEntity {

  /**
   * Retrieves the pickup status of the arrow entity.
   *
   * @return The arrow entity pickup status.
   */
  @FieldGetter("pickupStatus")
  Object getPickupStatus();

  /**
   * Changes the pickup status of the arrow entity.
   *
   * @param status The new pickup status.
   */
  @FieldSetter("pickupStatus")
  void setPickupStatus(Object status);

  /**
   * Retrieves the knockback strength of the arrow entity.
   *
   * @return The knockback strength.
   */
  @FieldGetter("knockbackStrength")
  int getKnockbackStrength();

  /**
   * Retrieves the arrow as an {@link ItemStack}.
   *
   * @return The arrow as an item stack.
   */
  @MethodProxy
  Object getArrowStack();

  /**
   * Retrieves the water drag of the arrow entity.
   *
   * @return The water drag of the arrow entity.
   */
  @MethodProxy
  float getWaterDrag();
}
