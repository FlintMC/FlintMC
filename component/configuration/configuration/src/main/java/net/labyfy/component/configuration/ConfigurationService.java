package net.labyfy.component.configuration;

public interface ConfigurationService {

  void load(Class<?> configurationClass);

  void save(Class<?> configurationClass);

}
