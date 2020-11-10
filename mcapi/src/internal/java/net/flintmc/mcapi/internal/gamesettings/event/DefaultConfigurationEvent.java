package net.flintmc.mcapi.internal.gamesettings.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.event.ConfigurationEvent;

import java.io.File;
import java.util.Map;

/** Default implementation of the {@link ConfigurationEvent}. */
@Implement(ConfigurationEvent.class)
public class DefaultConfigurationEvent implements ConfigurationEvent {

  private final State state;
  private File optionsFile;
  private Map<String, String> configurations;

  @AssistedInject
  private DefaultConfigurationEvent(
      @Assisted("state") State state,
      @Assisted("optionsFile") File optionsFile,
      @Assisted("configurations") Map<String, String> configurations) {
    this.state = state;
    this.optionsFile = optionsFile;
    this.configurations = configurations;
  }

  /** {@inheritDoc} */
  @Override
  public State getState() {
    return this.state;
  }

  /** {@inheritDoc} */
  @Override
  public File getOptionsFile() {
    return this.optionsFile;
  }

  /** {@inheritDoc} */
  @Override
  public void setOptionsFile(File optionsFile) {
    this.optionsFile = optionsFile;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, String> getConfigurations() {
    return this.configurations;
  }

  /** {@inheritDoc} */
  @Override
  public void setConfigurations(Map<String, String> configurations) {
    this.configurations = configurations;
  }
}
