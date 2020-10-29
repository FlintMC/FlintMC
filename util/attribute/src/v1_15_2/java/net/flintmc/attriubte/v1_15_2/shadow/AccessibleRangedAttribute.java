package net.flintmc.attriubte.v1_15_2.shadow;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow(" net.minecraft.entity.ai.attributes.RangedAttribute")
public interface AccessibleRangedAttribute {

  @FieldGetter("minimumValue")
  double getMinimumValue();

  @FieldGetter("maximalValue")
  double getMaximalValue();
}
