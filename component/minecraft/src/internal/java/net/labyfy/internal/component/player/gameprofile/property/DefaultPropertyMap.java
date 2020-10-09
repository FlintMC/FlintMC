package net.labyfy.internal.component.player.gameprofile.property;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
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

  @Inject
  public DefaultPropertyMap() {
    this.properties = LinkedHashMultimap.create();
  }

  @Override
  protected Multimap<String, Property> delegate() {
    return properties;
  }

}
