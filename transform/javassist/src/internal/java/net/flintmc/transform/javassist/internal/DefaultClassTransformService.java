package net.flintmc.transform.javassist.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.*;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  private final Logger logger;
  private final MethodBasedClassTransformMeta.Factory methodBasedClassTransformMetaFactory;
  private final ConsumerBasedClassTransformMeta.Factory consumerBasedClassTransformMetaFactory;
  private final List<ClassTransformMeta> classTransformMetas;

  @Inject
  private DefaultClassTransformService(
      @InjectLogger Logger logger,
      MethodBasedClassTransformMeta.Factory methodBasedClassTransformMetaFactory,
      ConsumerBasedClassTransformMeta.Factory consumerBasedClassTransformMetaFactory) {
    this.logger = logger;
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
    ClassPool classPool = ClassPool.getDefault();
    CtClass ctClass;

    try {
      ctClass =
          classPool.makeClass(
              new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), true);

      for (ClassTransformMeta classTransformMeta : this.classTransformMetas) {
        if (!classTransformMeta.matches(ctClass)) continue;
        classTransformMeta.execute(ctClass);
      }

      if(className.startsWith("net.minecraft.entity.LivingEntity"))
      Files.write(Paths.get(ctClass.getName() + ".class"), ctClass.toBytecode());

    } catch (IOException | CannotCompileException exception) {
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
