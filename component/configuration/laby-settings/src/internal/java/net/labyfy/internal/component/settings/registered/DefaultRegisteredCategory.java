package net.labyfy.internal.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.registered.RegisteredCategory;

@Implement(RegisteredCategory.class)
public class DefaultRegisteredCategory implements RegisteredCategory {

  private final String registryName;
  private final ChatComponent displayName;
  private final ChatComponent description;

  @AssistedInject
  public DefaultRegisteredCategory(@Assisted String registryName,
                                   @Assisted("displayName") ChatComponent displayName,
                                   @Assisted("description") ChatComponent description) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.description = description;
  }

  @Override
  public String getRegistryName() {
    return this.registryName;
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public ChatComponent getDescription() {
    return this.description;
  }

}
