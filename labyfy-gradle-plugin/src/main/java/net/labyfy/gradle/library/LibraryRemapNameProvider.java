package net.labyfy.gradle.library;

import org.objectweb.asm.commons.SimpleRemapper;

import java.util.List;

public class LibraryRemapNameProvider extends SimpleRemapper {

  private final SuperClassProvider superClassProvider;

  protected LibraryRemapNameProvider(LibraryRemapper remapper, SuperClassProvider superClassProvider) {
    super(remapper.getMappings());
    this.superClassProvider = superClassProvider;
  }

  public String mapMethodName(String owner, String name, String desc) {

    String map = this.map(owner.replace('/', '.') + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
    if (map == null) {

      List<String> possibleOwners = superClassProvider.getSuperClass(owner.replace('/', '.'));
      if (possibleOwners != null) {
        for (String possibleOwner : possibleOwners) {
          map = this.map(possibleOwner + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
          if (map != null) return map;
        }
      }
    }

    return map != null ? map : super.mapMethodName(owner, name, desc);
  }

  public String mapFieldName(String owner, String name, String desc) {
    // String map = this.map(owner + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
    // return map != null ? map : super.mapFieldName(owner, name, desc);
    return mapMethodName(owner, name, desc);
  }

  public String mapMethodDesc(String desc) {
    return super.mapMethodDesc(desc);
  }

  public String mapInvokeDynamicMethodName(String name, String desc) {
    return super.mapInvokeDynamicMethodName(name, desc);
  }

  public String map(String key) {
    return super.map(key);
  }


}
