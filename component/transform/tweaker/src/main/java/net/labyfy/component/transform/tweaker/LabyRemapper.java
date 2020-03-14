package net.labyfy.component.transform.tweaker;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.tweaker.mapping.ClassMappingProvider;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LabyRemapper extends SimpleRemapper {

  private final ClassMappingProvider classMappingProvider;
  private final Map<String, ClassNode> classes;

  private LabyRemapper(
      Map<String, String> mapping,
      ClassMappingProvider classMappingProvider,
      Map<String, ClassNode> classes) {
    super(mapping);
    this.classMappingProvider = classMappingProvider;
    this.classes = classes;
  }

  public String mapMethodName(String owner, String name, String desc) {

    owner = owner.replace('/', '.');
    String map = this.map(owner + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
    if (map == null) {

      List<String> possibleOwners = this.getSuperClass(owner.replace('/', '.'));

      for (String possibleOwner : possibleOwners) {
        map = this.map(classMappingProvider.get(possibleOwner).getUnObfuscatedName() + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
        if (map != null) return map;
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

  public List<String> getSuperClass(String clazz) {
    ClassNode theClazz =
        this.classes.get(
            this.classMappingProvider.get(clazz).getObfuscatedName().replace('.', '/'));
    if (theClazz == null) return Collections.emptyList();
    ArrayList<String> classes = new ArrayList<>();

    String superClazz = theClazz.superName;

    if (superClazz != null) {
      classes.add(superClazz);
    }
    if (theClazz.interfaces != null) {
      classes.addAll(theClazz.interfaces);
    }

    ArrayList<String> transitiveSuperClasses = new ArrayList<>();
    classes.forEach(c -> transitiveSuperClasses.addAll(getSuperClass(c)));
    classes.addAll(transitiveSuperClasses);
    return classes;
  }

  public static LabyRemapper create(
      Map<String, String> mapping,
      ClassMappingProvider classMappingProvider,
      Map<String, ClassNode> classes) {
    Preconditions.checkNotNull(mapping);
    Preconditions.checkNotNull(classes);
    Preconditions.checkNotNull(classes);
    return new LabyRemapper(mapping, classMappingProvider, classes);
  }
}
