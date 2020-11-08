package net.flintmc.processing.autoload.identifier;

import com.google.common.base.Functions;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s located at class level.
 *
 * @see Identifier
 */
public class ClassIdentifier implements Identifier<CtClass> {
  private final String name;
  private BiFunction<ClassIdentifier, String, String> nameConverter = (classIdentifier, name) -> name;

  public ClassIdentifier(String name) {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass getLocation() {
    try {
      return ClassPool.getDefault().get(this.getName());
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  public ClassIdentifier setNameConverter(@Nonnull BiFunction<ClassIdentifier, String, String> nameConverter) {
    this.nameConverter = nameConverter;
    return this;
  }

  /**
   * @return The class name of this identifier
   */
  public String getName() {
    return this.nameConverter.apply(this, this.name);
  }
}
