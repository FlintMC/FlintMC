/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.IOException;
import net.flintmc.framework.packages.PackageClassLoader;
import net.flintmc.framework.stereotype.service.CacheIdRetriever;
import net.flintmc.launcher.classloading.ClassTransformException;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoader;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.transform.asm.ASMUtils;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import net.flintmc.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import net.flintmc.util.classcache.ClassCache;
import net.flintmc.util.commons.Ref;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

/**
 * Deobfuscates all minecraft classes for which mappings are provided
 */
@Singleton
@MinecraftTransformer(priority = Integer.MIN_VALUE)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer, CacheIdRetriever {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassCache classCache;
  private final boolean obfuscated;

  private long cacheId;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper,
      @Named("obfuscated") boolean obfuscated,
      ClassCache classCache) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classCache = classCache;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  @Override
  public byte[] transform(String className, CommonClassLoader classLoader,
      byte[] classData) throws ClassTransformException {
    if (!this.obfuscated) {
      return classData;
    }
    if (!className.startsWith("net.flintmc.")
        && !(classLoader instanceof PackageClassLoader)) {
      // only reobfuscate flint classes and classes from packages
      return classData;
    }

    Ref<IOException> exception = new Ref<>();

    byte[] bytecode = this.classCache
        .getOrTransformAndWriteClass(className, this.cacheId, classData,
            b -> {
              ClassInformation classInformation = null;
              try {
                classInformation = CommonClassLoaderHelper
                    .retrieveClass(this.rootClassLoader, className);
              } catch (IOException e) {
                exception.set(e);
              }

              if (classInformation == null) {
                return classData;
              }

              ClassNode classNode = ASMUtils
                  .getNode(classInformation.getClassBytes());
              ClassWriter classWriter = new ClassWriter(
                  ClassWriter.COMPUTE_MAXS);
              ClassVisitor classRemapper = new ClassRemapper(classWriter,
                  this.minecraftClassRemapper);
              classNode.accept(classRemapper);
              return classWriter.toByteArray();
            });

    if (!exception.isNull()) {
      throw new ClassTransformException(
          "Unable to retrieve class metadata: " + className, exception.get());
    }

    return bytecode;
  }

  @Override
  public void setCacheId(long id) {
    this.cacheId = id;
  }
}
