package net.flintmc.mcapi.internal.player.gameprofile.property;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

/** An implementation of {@link PropertyMap} */
@Implement(PropertyMap.class)
public class DefaultPropertyMap extends ForwardingMultimap<String, Property>
    implements PropertyMap {

  private final Multimap<String, Property> properties;

  @AssistedInject
  private DefaultPropertyMap() {
    this.properties = LinkedHashMultimap.create();
  }

  @AssistedInject
  private DefaultPropertyMap(@Assisted("properties") Multimap<String, Property> properties) {
    this.properties = properties;
  }

  /** {@inheritDoc} */
  @Override
  protected Multimap<String, Property> delegate() {
    return properties;
  }
}
