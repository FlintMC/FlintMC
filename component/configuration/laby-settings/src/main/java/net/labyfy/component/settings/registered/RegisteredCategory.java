package net.labyfy.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface RegisteredCategory {

  String getRegistryName();

  ChatComponent getDisplayName();

  ChatComponent getDescription();

  String getIconUrl();

  @AssistedFactory(RegisteredCategory.class)
  interface Factory {

    RegisteredCategory create(@Assisted("registryName") String registryName,
                              @Assisted("displayName") ChatComponent displayName,
                              @Assisted("description") ChatComponent description,
                              @Assisted("iconUrl") String iconUrl);

  }

}
