package net.labyfy.internal.component.entity.v1_15_2.projectile;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.FieldSetter;
import net.labyfy.component.transform.shadow.MethodProxy;
import net.labyfy.component.transform.shadow.Shadow;

@Shadow("net.minecraft.entity.projectile.AbstractArrowEntity")
public interface AccessibleAbstractArrowEntity {

  @FieldGetter("pickupStatus")
  Object getPickupStatus();

  @FieldSetter("pickupStatus")
  void setPickupStatus(Object status);

  @FieldGetter("knockbackStrength")
  int getKnockbackStrength();

  @MethodProxy
  Object getArrowStack();

  @MethodProxy
  float getWaterDrag();

}
