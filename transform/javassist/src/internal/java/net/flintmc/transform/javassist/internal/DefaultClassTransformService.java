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

package net.flintmc.transform.javassist.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.ClassTransformMeta;
import net.flintmc.transform.javassist.ClassTransformService;
import net.flintmc.transform.javassist.ConsumerBasedClassTransformMeta;
import net.flintmc.transform.javassist.MethodBasedClassTransformMeta;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Singleton
@MinecraftTransformer
@Service(value = ClassTransform.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
@Implement(ClassTransformService.class)
public class DefaultClassTransformService
    implements ServiceHandler<ClassTransform>, LateInjectedTransformer, ClassTransformService {

  private final ClassPool pool;
  private final MethodBasedClassTransformMeta.Factory methodBasedClassTransformMetaFactory;
  private final ConsumerBasedClassTransformMeta.Factory consumerBasedClassTransformMetaFactory;
  private final List<ClassTransformMeta> classTransformMetas;

  @Inject
  private DefaultClassTransformService(
      ClassPool pool,
      MethodBasedClassTransformMeta.Factory methodBasedClassTransformMetaFactory,
      ConsumerBasedClassTransformMeta.Factory consumerBasedClassTransformMetaFactory) {
    this.pool = pool;
    this.methodBasedClassTransformMetaFactory = methodBasedClassTransformMetaFactory;
    this.consumerBasedClassTransformMetaFactory = consumerBasedClassTransformMetaFactory;
    this.classTransformMetas = new ArrayList<>();
  }

  @Override
  public void discover(AnnotationMeta<ClassTransform> identifierMeta)
      throws ServiceNotFoundException {
    this.classTransformMetas.add(this.methodBasedClassTransformMetaFactory.create(identifierMeta));
    this.sortFactories();
  }

  public ClassTransformService addClassTransformation(
      CtClass ctClass, Consumer<ClassTransformContext> consumer) {
    return this.addClassTransformation(ctClass, 0, consumer);
  }

  public ClassTransformService addClassTransformation(
      CtClass ctClass, int priority, Consumer<ClassTransformContext> consumer) {
    return this.addClassTransformation(
        this.consumerBasedClassTransformMetaFactory.create(ctClass, priority, consumer));
  }

  public ClassTransformService addClassTransformation(ClassTransformMeta classTransformMeta) {
    this.classTransformMetas.add(classTransformMeta);
    this.sortFactories();
    return this;
  }

  private void sortFactories() {
    this.classTransformMetas.sort(Comparator.comparingInt(ClassTransformMeta::getPriority));
  }

  @Override
  public byte[] transform(String className, byte[] bytes) throws ClassTransformException {
    CtClass ctClass;

    try {
      ctClass =
          this.pool.makeClass(
              new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), true);

      for (ClassTransformMeta classTransformMeta : this.classTransformMetas) {
        if (!classTransformMeta.matches(ctClass)) continue;
        classTransformMeta.execute(ctClass);
      }

    } catch (IOException exception) {
      throw new ClassTransformException("unable to read class", exception);
    }

    try {
      return ctClass.toBytecode();
    } catch (IOException exception) {
      // Basically unreachable.
      throw new ClassTransformException(
          "Unable to write class bytecode to byte array: " + className, exception);
    } catch (CannotCompileException exception) {
      throw new ClassTransformException("Unable to transform class: " + className, exception);
    }
  }
}
