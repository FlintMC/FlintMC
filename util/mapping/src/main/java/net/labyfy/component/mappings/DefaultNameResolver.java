package net.labyfy.component.mappings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.commons.resolve.NameResolver;

@Singleton
public class DefaultNameResolver implements NameResolver {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private DefaultNameResolver(final ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  public String resolve(String name) {
    ClassMapping classMapping = this.classMappingProvider.get(name);

    if (classMapping != null) {
      return classMapping.getName();
    }

    return null;
  }
}
