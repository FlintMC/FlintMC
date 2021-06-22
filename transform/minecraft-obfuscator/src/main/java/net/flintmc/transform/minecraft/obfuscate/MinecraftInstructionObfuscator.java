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

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.gradle.environment.DeobfuscationEnvironment;
import net.flintmc.gradle.environment.DeobfuscationException;
import net.flintmc.gradle.environment.EnvironmentCacheFileProvider;
import net.flintmc.gradle.java.instrumentation.api.InstrumentationTransformerRegistrator;
import net.flintmc.gradle.java.instrumentation.api.InstrumentationTransformerRegistry;
import net.flintmc.gradle.java.instrumentation.api.context.InstrumentationTransformerContext;
import net.flintmc.gradle.java.instrumentation.api.transformer.InstrumentationTransformer;
import net.flintmc.gradle.minecraft.MinecraftRepository;
import net.flintmc.gradle.minecraft.data.environment.MinecraftVersion;
import net.flintmc.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.util.Map;

import static net.flintmc.transform.minecraft.obfuscate.remap.MinecraftClassRemapper.getNode;

/**
 * Deobfuscates all minecraft classes for which mappings are provided
 */
@AutoService(InstrumentationTransformerRegistrator.class)
public class MinecraftInstructionObfuscator implements InstrumentationTransformerRegistrator, InstrumentationTransformer {

  private MinecraftClassRemapper classRemapper;


  @Override
  public void initialize(InstrumentationTransformerRegistry registry) {
    String minecraftVersion = (String) registry.getSourceSet().getExtensions().findByName("minecraftVersion");
    if (minecraftVersion == null) {
      return;
    }
    MinecraftVersion finalVersion = null;
    for (MinecraftVersion version : registry.getGradlePlugin().getExtension().getMinecraftVersions()) {
      if (version.getVersion().equals(minecraftVersion)) {
        finalVersion = version;
      }
    }
    if (finalVersion == null) {
      throw new AssertionError();
    }

    switch (finalVersion.getEnvironmentType()) {
      case MOD_CODER_PACK:
        break;
      default:
        throw new UnsupportedOperationException("Yarn mappings not implemented yet");
    }
    MinecraftRepository minecraftRepository = registry.getGradlePlugin().getMinecraftRepository();
    DeobfuscationEnvironment deobfuscationEnvironment = registry
        .getGradlePlugin()
        .getMinecraftRepository()
        .defaultEnvironment(finalVersion.getVersion(), finalVersion.getEnvironmentType());

    try {
      Map<String, File> mappingFiles = deobfuscationEnvironment
          .getDownloadedMappingFiles(
              registry.getGradlePlugin().getHttpClient(),
              new EnvironmentCacheFileProvider(minecraftRepository.getEnvironmentBasePath().resolve(deobfuscationEnvironment.name())));


      InjectionHolder.getInstance().addModules(new AbstractModule() {
        @Override
        protected void configure() {
          this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(ImmutableMap.of(
              "--game-version",
              minecraftVersion
          ));
          this.bindNamed("mappingPath", new String[]{
              mappingFiles.get("mcp-config").toString() + "/config",
              mappingFiles.get("mcp-mappings").toString()
          });
          this.bindNamedFilePath("flintRoot", "./flint");
          this.bindNamed("obfuscated", true);
        }

        private void bindNamedFilePath(String name, String path) {
          this.bindNamed(name, path);
          this.bindNamed(name, new File(path));
        }

        private void bindNamed(String name, Object object) {
          this.bind(Key.get(((Class) object.getClass()), Names.named(name))).toInstance(object);
        }
      });

      this.classRemapper = InjectionHolder.getInjectedInstance(MinecraftClassRemapper.class);
      registry.registerTransformer(this);
    } catch (DeobfuscationException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void transform(InstrumentationTransformerContext instrumentationTransformerContext) {
    ClassNode classNode = getNode(instrumentationTransformerContext.getData());
    ClassWriter classWriter = new ClassWriter(
        ClassWriter.COMPUTE_MAXS);
    ClassVisitor classRemapper = new ClassRemapper(classWriter,
        this.classRemapper);
    classNode.accept(classRemapper);

    instrumentationTransformerContext.setData(classWriter.toByteArray());
  }

  /*private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassCache classCache;
  private final boolean obfuscated;

  private long cacheId;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper,
      @Named("obfuscated") boolean obfuscated, ClassCache classCache) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classCache = classCache;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  @Override
  public byte[] transform(String className, CommonClassLoader classLoader,
      byte[] classData) throws ClassTransformException {
    if (!obfuscated) {
      return classData;
    }
    if (!className.startsWith("net.flintmc.")
        && !(classLoader instanceof PackageClassLoader)) {
      // only reobfuscate flint classes and classes from packages
      return classData;
    }

    final Ref<IOException> exception = new Ref<>();

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
                  minecraftClassRemapper);
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
  }*/
}
