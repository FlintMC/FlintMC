package net.labyfy.component.config.annotation;

import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * The {@link Config @Config} annotation can be used to automatically store the data in an interface into a file,
 * database or any other custom storage that has been implemented by some package (see {@link ConfigStorage}).
 * <p> <br>
 * Every config will automatically be read from the {@link ConfigStorageProvider} on start.
 * <p>
 * The implemented config can always be casted to {@link ParsedConfig} which gains access to all references that will be
 * stored.
 * <p>
 * The storages can be specified with the {@link IncludeStorage} and {@link ExcludeStorage} annotations, see {@link
 * ConfigObjectReference#appliesTo(ConfigStorage)} for more information.
 *
 * <br> <br>
 * If you don't want a config to be auto-generated and instead use an object from the injector, you can add {@link
 * ImplementedConfig @Implemented} to every interface within this config. Then the class that has been bound to the
 * annotated interface in the injector will be modified and methods to write the config on modification will be added.
 * For this to work, all the following methods are REQUIRED and NOT OPTIONAL because it is the only way to get all
 * values to store in the storages.
 * <p>
 * There are several methods that CAN (any method is optional, if the config is automatically generated, more
 * information in the paragraph above) be used in a config interface:
 * <p>
 * <ul>
 *   <li>
 * The first one is a simple getter with its setter:
 * <pre>
 *
 *   int getX();       // getter for X
 *
 *   void setX(int x); // setter for X
 *
 * </pre>
 * It will be identified by the 'get' or 'set' at the beginning, the getter needs to return the value to be stored and
 * the setter needs to have the value to be stored as its first (and only) parameter. Whenever the setter is called, the
 * changes will be automatically posted to {@link ConfigStorageProvider#write(ParsedConfig)} with ParsedConfig being the
 * instance of this config. Additionally, the setter needs to return {@code void}.
 * <p>
 * The type should be an interface, String, enum or any primitive.
 * </li>
 * <li>
 * The second one is also a getter and setter, but it works like a {@link Map}:
 * <pre>
 *
 *   Map&lt;MyEnum, Integer> getAllX();       // getter to get every value for X
 *
 *   void setAllX(Map&lt;MyEnum, Integer> x); // setter to set every value for X
 *
 *   int getX(MyEnum key);                    // getter to get one specific value for X
 *
 *   void setX(MyEnum key, int value);        // setter to set one specific value for X
 *
 * </pre>
 * It will be identified in two ways:
 * <ul>
 *   <li>
 *      The 'get' (1 parameter) or 'set' (2 parameters) at the beginning with the specific amount of parameters.
 *    </li>
 *    <li>
 *      The 'getAll' (0 parameters) and {@link Map} as its return type.
 *    </li>
 * </ul>
 * <p>
 * The type should be a String, enum or any primitive.
 * Whenever the setter is called, the changes will be automatically posted to {@link
 * ConfigStorageProvider#write(ParsedConfig)} with ParsedConfig being the instance of this config. Additionally, the
 * setters both need to return void.
 * </li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Config {

}
