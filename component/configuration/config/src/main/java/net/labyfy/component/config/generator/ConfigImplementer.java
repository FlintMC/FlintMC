package net.labyfy.component.config.generator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * This interface can add the {@link ParsedConfig} as an interface to a class and implement its methods.
 */
public interface ConfigImplementer {

  /**
   * Adds the {@link ParsedConfig} interface to the given class and implements all its methods.
   *
   * @param implementation The non-null class to add the interface to
   * @param name           The non-null name which should be returned by {@link ParsedConfig#getConfigName()}
   * @throws NotFoundException      If the {@link ParsedConfig} class cannot be found in the {@link
   *                                javassist.ClassPool}
   * @throws CannotCompileException If the generated source code cannot be compiled, should basically never happen
   */
  void implementParsedConfig(CtClass implementation, String name) throws NotFoundException, CannotCompileException;

}
