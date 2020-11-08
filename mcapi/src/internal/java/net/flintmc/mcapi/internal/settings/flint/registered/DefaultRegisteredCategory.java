package net.flintmc.mcapi.internal.settings.flint.registered;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;

@Implement(RegisteredCategory.class)
public class DefaultRegisteredCategory implements RegisteredCategory {

  private final String registryName;
  private final ChatComponent displayName;
  private final ChatComponent description;
  private final String iconUrl;

  @AssistedInject
  public DefaultRegisteredCategory(
      @Assisted("registryName") String registryName,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("description") ChatComponent description,
      @Assisted("iconUrl") String iconUrl) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.description = description;
    this.iconUrl = iconUrl;
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

  @Override
  public String getIconUrl() {
    return this.iconUrl;
  }
}
