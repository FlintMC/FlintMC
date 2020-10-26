package net.labyfy.internal.component.player.gameprofile.property;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.property.Property;
import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@link PropertyMap}
 */
@Implement(PropertyMap.class)
public class DefaultPropertyMap extends ForwardingMultimap<String, Property> implements PropertyMap {

  private final Multimap<String, Property> properties;

  @AssistedInject
  private DefaultPropertyMap() {
    this.properties = LinkedHashMultimap.create();
  }

  @AssistedInject
  private DefaultPropertyMap(@Assisted("properties") Multimap<String, Property> properties) {
    this.properties = properties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Multimap<String, Property> delegate() {
    return properties;
  }

}
