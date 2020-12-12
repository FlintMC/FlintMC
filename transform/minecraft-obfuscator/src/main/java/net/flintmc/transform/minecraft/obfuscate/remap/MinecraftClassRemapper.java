package net.flintmc.transform.minecraft.obfuscate.remap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.transform.asm.ASMUtils;
import net.flintmc.transform.minecraft.obfuscate.MinecraftInstructionObfuscator;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.MethodMapping;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and provides mappings for a {@link org.objectweb.asm.commons.ClassRemapper}, or in this
 * case {@link MinecraftInstructionObfuscator}.
 */
@Singleton
public class MinecraftClassRemapper extends SimpleRemapper {

  private final RootClassLoader rootClassLoader;
  private Handle lastHandle;

  @Inject
  private MinecraftClassRemapper(ClassMappingProvider classMappingProvider) {
    super(collectMappings(classMappingProvider));
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
    this.rootClassLoader.excludeFromModification("org.objectweb.asm.");
  }

  private static Map<String, String> collectMappings(ClassMappingProvider classMappingProvider) {
    Map<String, String> mappings = new HashMap<>();

    for (ClassMapping classMapping : classMappingProvider.getDeobfuscatedClassMappings().values()) {
      String name = classMapping.getDeobfuscatedName().replace('.', '/');

      if (!classMapping.isDefault()) {
        if (mappings.put(name, classMapping.getObfuscatedName().replace('.', '/')) != null) {
          throw new IllegalStateException("Duplicate key!");
        }
      }

      addMethodAndFieldMappings(mappings, classMapping, name);

      CtClass ctClass = null;

      try {
        ctClass = ClassPool.getDefault().get(classMapping.getName());
      } catch (NotFoundException ignored) {
        // Can be ignored, because the SRG mapping
        // contains classes which do not exist in the game.
      }

      if (ctClass != null) {
        try {
          CtClass superClass = ctClass.getSuperclass();

          while (superClass != null && superClass.getPackageName() == null) {
            ClassMapping superClassMapping = classMappingProvider.get(superClass.getName());

            // Adds all super types methods & fields to the given class
            addMethodAndFieldMappings(mappings, superClassMapping, name);

            superClass = superClass.getSuperclass();
          }

        } catch (NotFoundException exception) {
          // If the exception is thrown, the game is broken.
          exception.printStackTrace();
        }
      }
    }

    return mappings;
  }

  private static void addMethodAndFieldMappings(
      Map<String, String> mappings, ClassMapping classMapping, String name) {
    for (MethodMapping method : classMapping.getObfuscatedMethods().values()) {
      mappings.put(name + "." + method.getDeobfuscatedIdentifier(), method.getObfuscatedName());
    }

    for (FieldMapping value : classMapping.getObfuscatedFields().values()) {
      if (!value.isDefault()) {
        mappings.put(name + "." + value.getDeobfuscatedName(), value.getObfuscatedName());
      }
    }
  }

  public List<String> getSuperClass(String clazz) {
    try {
      ClassInformation classInformation =
          CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, clazz);
      if (classInformation == null) {
        // Java internal class
        return new ArrayList<>();
      }

      ClassNode theClazz = ASMUtils.getNode(classInformation.getClassBytes());

      ClassInformation superClassInformation =
          CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, theClazz.superName);

      ClassNode superClass =
          superClassInformation == null
              ? null
              : ASMUtils.getNode(superClassInformation.getClassBytes());

      ArrayList<String> classes = new ArrayList<>();
      if (superClass != null) {
        classes.add(superClass.name);
      }
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

  @Override
  public String map(String key) {
    return super.map(key);
  }

  @Override
  public String mapMethodName(String owner, String name, String desc) {

    String map =
        this.map(
            owner.replace('.', '/') + "." + name + desc.substring(0, desc.lastIndexOf(')') + 1));
    if (map == null) {

      List<String> possibleOwners = this.getSuperClass(owner.replace('.', '/'));
      if (possibleOwners != null) {
        for (String possibleOwner : possibleOwners) {
          map =
              this.map(
                  possibleOwner.replace('.', '/')
                      + "."
                      + name
                      + desc.substring(0, desc.lastIndexOf(')') + 1));
          if (map != null) {
            return map;
          }
        }
      }
    }

    return map != null ? map : super.mapMethodName(owner, name, desc);
  }

  @Override
  public String mapInvokeDynamicMethodName(String name, String desc) {
    if (lastHandle != null && lastHandle.getName().startsWith("lambda$")) {
      String owner = Type.getReturnType(desc).getInternalName();
      String descriptor = this.lastHandle.getDesc();
      String methodName = this.mapMethodName(owner, name, descriptor);
      lastHandle = null;
      return methodName;
    }
    lastHandle = null;
    return super.mapInvokeDynamicMethodName(name, desc);
  }

  @Override
  public Object mapValue(Object value) {
    if (value instanceof Handle) {
      lastHandle = (Handle) value;
    }
    return super.mapValue(value);
  }

  @Override
  public String mapFieldName(String owner, String name, String desc) {
    if (name.startsWith("$SwitchMap$")) {
      String switchMap = name.replace("$SwitchMap$", "");
      switchMap = switchMap.replace('$', '/');
      switchMap = mapType(switchMap);
      switchMap = switchMap.replace('/', '$');
      switchMap = "$SwitchMap$" + switchMap;
      return switchMap;
    }

    return mapMethodName(owner, name, desc);
  }

  @Override
  public String mapMethodDesc(String desc) {
    return super.mapMethodDesc(desc);
  }
}
