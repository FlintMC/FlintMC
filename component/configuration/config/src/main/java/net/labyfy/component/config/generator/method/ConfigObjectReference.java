package net.labyfy.component.config.generator.method;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.IncludeStorage;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.modifier.ConfigModifierRegistry;
import net.labyfy.component.config.serialization.ConfigSerializationService;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Represents the path to a value in a {@link Config} containing methods to get/change the value of the entry in this
 * config.
 */
public interface ConfigObjectReference {

  /**
   * Retrieves the Key which is unique per {@link Config}. It is the {@link #getPathKeys()} separated by a '.'. It
   * always contains at least one entry because {@code #getPathKeys().length} is always {@code > 0}.
   *
   * @return The non-null key string
   */
  String getKey();

  /**
   * Retrieves the last element in {@link #getPathKeys()}, which is for example for the method 'getX' 'X', see {@link
   * Config} for more information on how this is parsed.
   *
   * @return The non-null name of the last entry of this reference
   */
  String getLastName();

  /**
   * Retrieves an array of path entries, this array and its contents are unique per {@link Config} and the length of it
   * is always {@code > 0}.
   *
   * @return The non-null and non-empty array of path entries
   */
  String[] getPathKeys();

  /**
   * Retrieves the class where the getter/setter are declared.
   *
   * @return The non-null class of the getter/setter
   */
  Class<?> getDeclaringClass();

  /**
   * Retrieves the config where this reference has been discovered.
   *
   * @return The non-null config of this reference
   */
  ParsedConfig getConfig();

  /**
   * @return The non-null type for serialization
   * @see ConfigMethod#getSerializedType()
   */
  Type getSerializedType();

  /**
   * Finds the last annotation on every method or interface associated with this reference.
   * <p>
   * Associated methods are methods, that are either in the {@link #getPathKeys()} array, associated interfaces are
   * interfaces, that contain the specified methods or any superinterface.
   * <p>
   * If the given annotation is not applied to any of those, {@code null} will be returned. If any is specified, the
   * last one will be used always.
   * <p>
   * Methods have a higher priority than Interfaces.
   * <p>
   * The annotations may be modified by the {@link ConfigModifierRegistry}.
   *
   * @param annotationType The non-null type of the annotation
   * @param <A>            The annotation which should be searched
   * @return The discovered annotation or {@code null} if no method/interface associated with this reference is
   * annotated with it.
   */
  <A extends Annotation> A findLastAnnotation(Class<? extends A> annotationType);

  /**
   * Finds all annotations on every method or interface associated with this reference.
   * <p>
   * Associated methods are methods, that are either in the {@link #getPathKeys()} array, associated interfaces are
   * interfaces, that contain the specified methods or any superinterface.
   * <p>
   * If the given annotation is not applied to any of those, an empty collection will be returned.
   * <p>
   * The annotations may be modified by the {@link ConfigModifierRegistry}.
   *
   * @return The non-null collection of all annotations on the associated methods and interfaces
   */
  Collection<Annotation> findAllAnnotations();

  /**
   * Checks whether the given storage is applied to any of the methods in {@link #getPathKeys()}.
   * <p>
   * The storages can be specified with the {@link IncludeStorage} and {@link ExcludeStorage} annotations, those can be
   * added to any method or interface associated with the value. If none of these annotations is applied, it will be
   * stored in every storage. If any is specified, the last one will be used always, so for example:
   *
   * <pre>
   * {@literal @}IncludeStorage("abcd")
   *  interface Config {
   *
   *   {@literal @}IncludeStorage("1234")
   *    String getX();
   *
   *   {@literal @}IncludeStorage("5678")
   *    void setX();
   *
   *    int getY();
   *
   *   {@literal @}ExcludeStorage("local")
   *    boolean getZ();
   *
   *   }
   * </pre>
   * <p>
   * So in this example
   * <br>
   * - 'X' will be stored in the storage '5678' because '5678' is the last method/interface with the IncludeStorage
   * annotation.
   * <br>
   * - 'Y' will be stored in the storage 'abcd' because the method itself is not annotated, but the interface is. If the
   * Config interface wouldn't be annotated with {@link Config @Config}, 'Y' would be stored in every storage.
   * <br>
   * - 'Z' will be stored in every storage except 'local' because the method is annotated with {@link ExcludeStorage
   * {@literal @}ExcludeStorage} and {@link ExcludeStorage} always has a higher priority than {@link IncludeStorage}.
   *
   * @param storage The non-null storage which should be checked for
   * @return Whether this reference should be saved in this storage or not
   */
  boolean appliesTo(ConfigStorage storage);

  /**
   * Retrieves the default value which has been defined by the user and will be used if no value is set in the config.
   * It is parsed from the {@link DefaultAnnotationMapperRegistry}.
   *
   * @return The default value or {@code null} if no defaultValue has been set
   */
  Object getDefaultValue();

  /**
   * Retrieves the value of the method which is linked to this reference in the instance of a config.
   *
   * @return The value from the config, may be {@code null}
   */
  Object getValue();

  /**
   * Changes the value of the method which is linked to this reference in the given instance of a config.
   *
   * @param value The value to set in the config, may be {@code null} depending on the implementation. For any primitive
   *              it should not be null
   */
  void setValue(Object value);

  /**
   * Parser for one or multiple {@link ConfigObjectReference ConfigObjectReference(s)}.
   */
  interface Parser {

    /**
     * Parses a single reference out of the given method with all necessary information for serialization like the
     * getter, setter and serialized type.
     *
     * @param generatingConfig The non-null config which contains the given {@code method}
     * @param config           The non-null instance of the config that has been generated
     * @param method           The non-null method to parse the reference from
     * @return The new non-null reference
     * @throws RuntimeException If any of the methods in the path specified in {@link ConfigMethod#getPathPrefix()}
     *                          doesn't exist
     */
    ConfigObjectReference parse(GeneratingConfig generatingConfig, ParsedConfig config, ConfigMethod method) throws RuntimeException;

    /**
     * Calls {@link #parse(GeneratingConfig, ParsedConfig, ConfigMethod)} for every method in the given config ({@link
     * GeneratingConfig#getAllMethods()}) if the stored in type in the specific method is not an interface and not
     * {@link ConfigSerializationService#hasSerializer(CtClass)}.
     *
     * @param generatingConfig The non-null config to parse the references from
     * @param config           The non-null instance of the config that has been generated
     * @return The new non-null list of the parsed references
     * @throws RuntimeException If any of the methods in the path specified in {@link ConfigMethod#getPathPrefix()}
     *                          doesn't exist
     */
    Collection<ConfigObjectReference> parseAll(GeneratingConfig generatingConfig, ParsedConfig config) throws RuntimeException;

  }

  /**
   * Factory for the {@link ConfigObjectReference}.
   */
  @AssistedFactory(ConfigObjectReference.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigObjectReference} with the specified values.
     *
     * @param generatingConfig     The non-null generating config with all information about the generated config
     * @param config               The non-null instance of the config that has been generated
     * @param pathKeys             The non-null array of keys to the new reference (unique per {@link
     *                             GeneratingConfig})
     * @param path                 The non-null array of methods (matches the {@code pathKeys} array) to the new
     *                             reference
     * @param correspondingMethods All methods that correspond to this reference and where annotations should be
     *                             searched in (e.g. 'getX', 'setX', 'getAllX', ...)
     * @param getter               The non-null getter method, needs to be in the interface of the return type of the
     *                             last entry in the {@code path} array (or if the array is empty, in the base class
     *                             from the config)
     * @param setter               The non-null setter method, needs to be in the interface of the return type of the
     *                             last entry in the {@code path} array (or if the array is empty, in the base class
     *                             from the config)
     * @param classLoader          The non-null class loader to load the classes of the CtMethods
     * @param serializedType       The type to for serialization
     * @return The new non-null {@link ConfigObjectReference}
     */
    ConfigObjectReference create(@Assisted GeneratingConfig generatingConfig, @Assisted ParsedConfig config,
                                 @Assisted("pathKeys") String[] pathKeys, @Assisted("path") CtMethod[] path,
                                 @Assisted("correspondingMethods") CtMethod[] correspondingMethods,
                                 @Assisted("getter") CtMethod getter, @Assisted("setter") CtMethod setter,
                                 @Assisted("declaringClass") String declaringClass,
                                 @Assisted("classLoader") ClassLoader classLoader,
                                 @Assisted("serializedType") Type serializedType);

  }

}
