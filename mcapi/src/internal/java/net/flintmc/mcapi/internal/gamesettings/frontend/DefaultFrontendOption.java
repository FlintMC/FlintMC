package net.flintmc.mcapi.internal.gamesettings.frontend;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.frontend.FrontendOption;

/** Default implementation of a {@link FrontendOption}. */
@Implement(FrontendOption.class)
public class DefaultFrontendOption implements FrontendOption {

  private final String name;
  private final Class<?> type;
  private final String defaultValue;

  @AssistedInject
  protected DefaultFrontendOption(
      @Assisted("name") String name,
      @Assisted("type") Class<?> type,
      @Assisted("defaultValue") String defaultValue) {
    this.name = name;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  /** {@inheritDoc} */
  @Override
  public String getConfigurationName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> getType() {
    return this.type;
  }

  /** {@inheritDoc} */
  @Override
  public String getDefaultValue() {
    return this.defaultValue;
  }
}