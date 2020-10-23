package net.labyfy.component.config.generator.method;

import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.GeneratingConfig;

/**
 * Parses all {@link ConfigMethod}s in an interface and all sub interfaces.
 */
public interface ConfigMethodResolver {

  /**
   * Parses all {@link ConfigMethod}s from all interfaces that are referenced in the interface of {@link
   * GeneratingConfig#getBaseClass()}. The methods will then be added to {@link GeneratingConfig#getAllMethods()}.
   *
   * @param config The non-null config to parse the methods for
   * @throws NotFoundException If an internal error occurred when trying to find a CtClass/CtMethod
   * @see Config
   */
  void resolveMethods(GeneratingConfig config) throws NotFoundException;

}
