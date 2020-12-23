package net.flintmc.mcapi.internal.settings.flint.registered;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;

/**
 * {@inheritDoc}
 */
@Implement(RegisteredCategory.class)
public class DefaultRegisteredCategory implements RegisteredCategory {

  private final String registryName;
  private final ChatComponent displayName;
  private final ChatComponent description;
  private final Icon icon;

  @AssistedInject
  public DefaultRegisteredCategory(
      @Assisted("registryName") String registryName,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("description") ChatComponent description,
      @Assisted("icon") Icon icon) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRegistryName() {
    return this.registryName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDescription() {
    return this.description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return this.icon;
  }
}
