package net.labyfy.component.mappings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.commons.resolve.NameResolver;

@Singleton
public class DefaultNameResolver implements NameResolver {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private DefaultNameResolver(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  public String resolve(String name) {
    return this.classMappingProvider.get(name).getName();
  }
}
