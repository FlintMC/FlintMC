package net.labyfy.component.session;

import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.UUID;

public interface ProfileLoader {

  PropertyMap loadProfileProperties(UUID uniqueId);

}
