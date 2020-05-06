package net.labyfy.component.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.classloading.common.ClassInformation;
import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.labyfy.component.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

@Singleton
@MinecraftTransformer(priority = Integer.MAX_VALUE)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassMappingProvider classMappingProvider;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper, ClassMappingProvider classMappingProvider) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classMappingProvider = classMappingProvider;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  public byte[] transform(String className, byte[] classData) {
    try {

      if (!className.startsWith("net.labyfy")) return classData;

      ClassInformation classInformation =
          CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, className);
      if (classInformation == null) return classData;

      ClassNode classNode = getNode(classInformation.getClassBytes());
      ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor classRemapper = new ClassRemapper(classWriter, minecraftClassRemapper);
      classNode.accept(classRemapper);
      return classWriter.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return classData;
  }

  private ClassNode getNode(final byte[] bytez) {
    ClassReader cr = new ClassReader(bytez);
    ClassNode cn = new ClassNode();
    try {
      cr.accept(cn, ClassReader.EXPAND_FRAMES);
    } catch (Exception e) {
      try {
        cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
      } catch (Exception e2) {
        // e2.printStackTrace();
      }
    }
    cr = null;
    return cn;
  }
}
