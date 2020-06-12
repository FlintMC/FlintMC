package net.labyfy.gradle.library;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.FieldMapping;
import net.labyfy.component.mappings.MethodMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class LibraryRemapper {

  private final Map<String, String> mappings = Maps.newConcurrentMap();
  private final Injector injector;
  private ClassMappingProvider classMappingProvider;

  private Map<String, byte[]> bytes;

  @Inject
  private LibraryRemapper(Injector injector) {
    this.injector = injector;
  }

  public void remap(File file, Collection<File> libraries) {
    try {
      this.classMappingProvider = this.injector.getInstance(ClassMappingProvider.class);
      this.loadMappings();
      new LibraryRemapperContext(new LibraryRemapNameProvider(this, new ReflectionSuperClassProvider(file, libraries)), this, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> loadMappings() {
    this.loadClassMappings();
    this.loadMethodMappings();
    this.loadFieldMappings();
    return this.mappings;
  }

  private void loadFieldMappings() {
    this.mappings.putAll(
        this.classMappingProvider.getObfuscatedClassMappings().values().stream()
            .flatMap(classMapping -> classMapping.getFields().stream())
            .collect(
                Collectors.toMap(
                    field ->
                        field.getClassMapping().getObfuscatedName()
                            + "."
                            + field.getObfuscatedFieldName(),
                    FieldMapping::getUnObfuscatedFieldName)));
  }

  private void loadMethodMappings() {
    this.mappings.putAll(
        this.classMappingProvider.getObfuscatedClassMappings().values().stream()
            .flatMap(classMapping -> classMapping.getMethods().stream())
            .collect(
                Collectors.toMap(
                    method ->
                        method.getClassMapping().getObfuscatedName()
                            + "."
                            + method.getObfuscatedMethodIdentifier(),
                    MethodMapping::getUnObfuscatedMethodName)));
  }

  private void loadClassMappings() {
    this.classMappingProvider
        .getObfuscatedClassMappings()
        .forEach(
            (obfuscatedName, mapping) ->
                this.mappings.put(
                    obfuscatedName.replace('.', '/'),
                    mapping.getUnObfuscatedName().replace('.', '/')));
  }


  protected Map<String, String> getMappings() {
    return mappings;
  }
}
