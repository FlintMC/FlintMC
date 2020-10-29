package net.flintmc.attribute.mapper;

import net.flintmc.attribute.Attribute;

public interface AttributeMapper {

  Object toMinecraftAttribute(Attribute attribute);

  Attribute fromMinecraftAttribute(Object handle);

}
