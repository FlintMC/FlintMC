package net.flintmc.attriubte.v1_15_2.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.attribute.Attribute;
import net.flintmc.attribute.attributes.RangedAttribute;
import net.flintmc.attribute.mapper.AttributeMapper;
import net.flintmc.attriubte.v1_15_2.shadow.AccessibleRangedAttribute;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(value = AttributeMapper.class, version = "1.15.2")
public class VersionedAttributeMapper implements AttributeMapper {

  private final RangedAttribute.Factory rangedAttributeFactory;

  @Inject
  private VersionedAttributeMapper(RangedAttribute.Factory rangedAttributeFactory) {
    this.rangedAttributeFactory = rangedAttributeFactory;
  }

  @Override
  public Object toMinecraftAttribute(Attribute attribute) {
    return null;
  }

  @Override
  public Attribute fromMinecraftAttribute(Object handle) {

    net.minecraft.entity.ai.attributes.RangedAttribute rangedAttribute = (net.minecraft.entity.ai.attributes.RangedAttribute) handle;
    AccessibleRangedAttribute accessibleRangedAttribute = (AccessibleRangedAttribute) rangedAttribute;

    return this.rangedAttributeFactory.create(
            rangedAttribute.getName(),
            rangedAttribute.getDefaultValue(),
            rangedAttribute.getDescription(),
            accessibleRangedAttribute.getMinimumValue(),
            accessibleRangedAttribute.getMaximalValue()
    );
  }


}
