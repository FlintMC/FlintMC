package net.flintmc.attribute.collect;

import net.flintmc.attribute.AttributeFoundation;

import java.util.Collection;
import java.util.Set;

public interface AttributeMap<T extends AttributeFoundation> extends AttributeFoundationMap<T> {

  /**
   * Retrieves a collection with all instances of the attribute map.
   *
   * @return A collection with all instances.
   */
  Set<AttributeFoundation> getInstances();

  /**
   * Retrieves a collection with all watched attributes of the attribute map.
   *
   * @return A collection with all watched attributes.
   */
  Collection<AttributeFoundation> getWatchedAttributes();

}
