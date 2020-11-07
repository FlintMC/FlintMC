package net.labyfy.component.config.generator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.method.ConfigObjectReference.Parser;
import net.labyfy.component.config.storage.ConfigStorageProvider;

import java.io.IOException;
import java.util.Collection;

/**
 * Generator for implementations of interfaces annotated with {@link Config}.
 */
public interface ConfigGenerator {

  /**
   * Generates a new implementation for the given interface, constructs a new instance of it and fills the {@link
   * ParsedConfig#getConfigReferences()} collection with the references in the given interface parsed from the {@link
   * Parser}. The instance will then be added to {@link #getDiscoveredConfigs()}.
   * <p> <br>
   * Since every config can only be implemented once (names of the configs would be duplicated and therefore cause
   * problems in the storages), it will return the implementation that has been generated recently if there is one in
   * {@link #getDiscoveredConfigs()}.
   *
   * @param configInterface The non-null interface to create an implementation for
   * @return The new non-null implementation of the given interface
   * @throws NotFoundException            If any method is not found, should basically never occur
   * @throws CannotCompileException       If generated code cannot be compiled, should basically never occur
   * @throws IOException                  If an I/O error occurred while writing the bytecode
   * @throws ReflectiveOperationException If a reflective error occurred while creating a new instance of the new
   *                                      implementation
   */
  ParsedConfig generateConfigImplementation(CtClass configInterface)
      throws NotFoundException, CannotCompileException, IOException, ReflectiveOperationException;

  /**
   * Retrieves a collection with all configs that have been parsed from {@link #generateConfigImplementation(CtClass)}
   * in this generator.
   *
   * @return An unmodifiable collection of all generated configs
   */
  Collection<ParsedConfig> getDiscoveredConfigs();

  /**
   * Adds the given config to the {@link #getDiscoveredConfigs()} collection and fills the {@link
   * ParsedConfig#getConfigReferences()} references} with the references parsed from the given {@link GeneratingConfig}.
   * Additionally, the config filled with the references will be forwareded to {@link
   * ConfigStorageProvider#read(ParsedConfig)} to fill the config with values from the storages and the {@link
   * ConfigDiscoveredEvent} will be fired.
   *
   * @param generatingConfig The non-null generating config with all methods to parse the references from to register
   *                         the config
   * @param config           The non-null config to be registered
   * @throws IllegalStateException If a config with the {@link ParsedConfig#getConfigName() given name} is already
   *                               registered
   */
  void bindConfig(GeneratingConfig generatingConfig, ParsedConfig config) throws IllegalStateException;

  /**
   * Should only be called once per config, and is only intended to be used internally. This method sets all default
   * values to the config and reads it from the config.
   *
   * @param config The non-null config to be initialized
   */
  void initConfig(ParsedConfig config);

}
