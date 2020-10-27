package net.flintmc.transform.minecraft.obfuscate.remap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.transform.minecraft.obfuscate.MinecraftInstructionObfuscator;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.MethodMapping;
import net.flintmc.transform.asm.ASMUtils;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Loads and provides mappings for a {@link org.objectweb.asm.commons.ClassRemapper},
 * or in this case {@link MinecraftInstructionObfuscator}.
 */
@Singleton
public class MinecraftClassRemapper extends SimpleRemapper {

  private final ClassMappingProvider classMappingProvider;
  private final RootClassLoader rootClassLoader;

  @Inject
  private MinecraftClassRemapper(ClassMappingProvider classMappingProvider) {
    super(
        collectMappings(classMappingProvider));
    this.classMappingProvider = classMappingProvider;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  private static Map<String, String> collectMappings(ClassMappingProvider classMappingProvider) {
    Map<String, String> mappings = new HashMap<>();
    mappings.putAll(classMappingProvider.getDeobfuscatedClassMappings().values().stream()
        .filter(classMapping -> !classMapping.isDefault())
        .collect(
            Collectors.toMap(
                classMapping -> classMapping.getDeobfuscatedName().replace('.', '/'),
                classMapping -> classMapping.getObfuscatedName().replace('.', '/')))
    );


    List<MethodMapping> methodMappings = new ArrayList<>();

    for (ClassMapping classMapping : classMappingProvider.getDeobfuscatedClassMappings().values()) {
      for (MethodMapping method : classMapping.getObfuscatedMethods().values()) {
        if (mappings.containsKey(method.getClassMapping().getDeobfuscatedName().replace('.', '/') + "." + method.getDeobfuscatedIdentifier())) {
          methodMappings.add(method);
        } else {
          mappings.put(method.getClassMapping().getDeobfuscatedName().replace('.', '/') + "." + method.getDeobfuscatedIdentifier(), method.getObfuscatedName());
        }
      }
    }

    mappings.putAll(classMappingProvider.getDeobfuscatedClassMappings().values().stream()
        .map(classMapping -> classMapping.getObfuscatedFields().values())
        .flatMap(Collection::stream)
        .filter(fieldMapping -> !fieldMapping.isDefault())
        .collect(
            Collectors.toMap(
                fieldMapping -> fieldMapping.getClassMapping().getDeobfuscatedName().replace('.', '/') + "." + fieldMapping.getDeobfuscatedName(),
                FieldMapping::getObfuscatedName)
        ));
    return mappings;
  }

  public List<String> getSuperClass(String clazz) {
    try {
      ClassNode theClazz =
          ASMUtils.getNode(
              CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, clazz).getClassBytes());

      ClassInformation superClassInformation =
          CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, theClazz.superName);

      ClassNode superClass =
          superClassInformation == null
              ? null
              : ASMUtils.getNode(superClassInformation.getClassBytes());

      ArrayList<String> classes = new ArrayList<>();
      if (superClass != null) classes.add(superClass.name);
      if (theClazz.interfaces != null) {
        classes.addAll(theClazz.interfaces);
      }

      ArrayList<String> transitiveSuperClasses = new ArrayList<>();
      classes.forEach(c -> transitiveSuperClasses.addAll(getSuperClass(c)));
      classes.addAll(transitiveSuperClasses);
      return classes;
    } catch (Exception ignored) {
      // Not found, can be ignored
      return new ArrayList<>();
    }
  }

  public String map(String key) {
    return super.map(key);
  }

  public String mapMethodName(String owner, String name, String desc) {

    String map = this.map(owner.replace('.', '/') + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
    if (map == null) {

      List<String> possibleOwners = this.getSuperClass(owner.replace('.', '/'));
      if (possibleOwners != null) {
        for (String possibleOwner : possibleOwners) {
          map = this.map(possibleOwner.replace('.', '/') + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
          if (map != null) return map;
        }
      }
    }

    return map != null ? map : super.mapMethodName(owner, name, desc);
  }

  public String mapInvokeDynamicMethodName(String name, String desc) {
    return super.mapInvokeDynamicMethodName(name, desc);
  }

  public String mapFieldName(String owner, String name, String desc) {
    return mapMethodName(owner, name, desc);
  }

  public String mapMethodDesc(String desc) {
    return super.mapMethodDesc(desc);
  }
}
