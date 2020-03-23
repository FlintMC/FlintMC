package net.labyfy.component.transform.asm;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

@Singleton
@MinecraftTransformer
@Service(MethodVisit.class)
public class MethodVisitService implements ServiceHandler, IClassTransformer {

  private final Collection<MethodVisitorContext> methodVisitorContexts;
  private final ClassMappingProvider classMappingProvider;

  @Inject
  private MethodVisitService(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
    this.methodVisitorContexts = Sets.newConcurrentHashSet();
  }

  public byte[] transform(String s, String s1, byte[] bytes) {
    ClassMapping classMapping = classMappingProvider.get(s);
    for (MethodVisitorContext methodVisitorContext : this.methodVisitorContexts) {
      MethodVisit methodVisit = methodVisitorContext.getMethodVisit();
      String className = methodVisit.className();

      if (!(className.equals(classMapping.getObfuscatedName())
          || className.equals(classMapping.getUnObfuscatedName()))) continue;

      ClassReader classReader = new ClassReader(bytes);
      ClassWriter classWriter = new ClassWriter(classReader, 3);

      ClassVisitor classVisitor =
          new ClassVisitor(Opcodes.ASM4, classWriter) {
            public MethodVisitor visitMethod(
                int access, String name, String desc, String signature, String[] exceptions) {

              MethodMapping methodMapping =
                  classMapping.getMethod(name + desc.substring(0, desc.lastIndexOf(')') + 1));

              if (methodMapping.isDefault()) {
                if (name.equals("<init>")) {
                  methodMapping =
                      MethodMapping.create(
                          classMapping, "<init>", "<init>", "<init>", "<init>", "<init>", "<init>");
                } else {
                  methodMapping = classMapping.getMethod(name);
                }
              }

              if (methodMapping != null) {
                for (MethodVisitorContext methodVisitorContext : methodVisitorContexts) {
                  if (methodVisitorContext.getMethodVisit().desc().isEmpty()
                      || methodMapping
                          .getObfuscatedMethodDescription()
                          .equals(methodVisitorContext.getMethodVisit().desc())
                      || methodMapping
                          .getUnObfuscatedMethodDescription()
                          .equals(methodVisitorContext.getMethodVisit().desc())) {

                    if (methodVisitorContext.getMethodVisit().methodName().isEmpty()
                        || methodMapping
                            .getObfuscatedMethodName()
                            .equals(methodVisitorContext.getMethodVisit().methodName())
                        || methodMapping
                            .getUnObfuscatedMethodName()
                            .equals(methodVisitorContext.getMethodVisit().methodName())) {
                      methodVisitorContext.setMethodVisitor(
                          new MethodVisitor(
                              Opcodes.ASM5,
                              super.visitMethod(access, name, desc, signature, exceptions)) {});
                      return methodVisitorContext;
                    }
                  }
                }
              }
              return super.visitMethod(access, name, desc, signature, exceptions);
            }
          };

      classReader.accept(classVisitor, 0);

      return classWriter.toByteArray();
    }

    return bytes;
  }

  public void discover(Identifier.Base property) {
    MethodVisit methodVisit =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    MethodVisitorContext methodVisitorContext = new MethodVisitorContext(methodVisit);
    Method location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    try {
      location.invoke(
          InjectionHolder.getInjectedInstance(location.getDeclaringClass()),
          methodVisitorContext);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    this.methodVisitorContexts.add(methodVisitorContext);
  }
}