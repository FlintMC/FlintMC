package net.labyfy.internal.component.transform.asm;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.asm.MethodVisit;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import org.objectweb.asm.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

@Singleton
@MinecraftTransformer
@Service(MethodVisit.class)
public class MethodVisitService implements ServiceHandler, LateInjectedTransformer {
  private final boolean obfuscated = InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")));
  private final Collection<InternalMethodVisitorContext> methodVisitorContexts;
  private final ClassMappingProvider classMappingProvider;

  @Inject
  private MethodVisitService(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
    this.methodVisitorContexts = Sets.newConcurrentHashSet();
  }

  @Override
  public byte[] transform(String s, byte[] bytes) {
    ClassMapping classMapping = classMappingProvider.get(s);
    for (MethodVisitorContext methodVisitorContext : this.methodVisitorContexts) {
      MethodVisit methodVisit = methodVisitorContext.getMethodVisit();
      String className = methodVisit.className();

      if (!(className.equals(classMapping.getObfuscatedName())
          || className.equals(classMapping.getDeobfuscatedName()))) continue;

      ClassReader classReader = new ClassReader(bytes);
      ClassWriter classWriter = new ClassWriter(classReader, 3);

      ClassVisitor classVisitor =
          new ClassVisitor(Opcodes.ASM4, classWriter) {
            public MethodVisitor visitMethod(
                int access, String name, String desc, String signature, String[] exceptions) {

              MethodMapping methodMapping =
                  classMapping.getMethodByIdentifier(name + desc.substring(0, desc.lastIndexOf(')') + 1));

              if (methodMapping == null) {
                if (name.equals("<init>")) {
                  methodMapping = new MethodMapping(obfuscated, classMapping, "<init>", "<init>", "<init>", "<init>");
                }
              }

              if (methodMapping != null) {
                for (InternalMethodVisitorContext methodVisitorContext : methodVisitorContexts) {
                  if (methodVisitorContext.getMethodVisit().desc().isEmpty()
                      || methodMapping
                      .getObfuscatedDescriptor()
                      .equals(methodVisitorContext.getMethodVisit().desc())
                      || methodMapping
                      .getDeobfuscatedDescriptor()
                      .equals(methodVisitorContext.getMethodVisit().desc())) {

                    if (methodVisitorContext.getMethodVisit().methodName().isEmpty()
                        || methodMapping
                        .getObfuscatedName()
                        .equals(methodVisitorContext.getMethodVisit().methodName())
                        || methodMapping
                        .getDeobfuscatedName()
                        .equals(methodVisitorContext.getMethodVisit().methodName())) {
                      methodVisitorContext.setMethodVisitor(
                          new MethodVisitor(
                              Opcodes.ASM5,
                              super.visitMethod(access, name, desc, signature, exceptions)) {
                          });
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

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {
    MethodVisit methodVisit =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    InternalMethodVisitorContext methodVisitorContext = new InternalMethodVisitorContext(methodVisit);
    Method location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    try {
      location.invoke(
          InjectionHolder.getInjectedInstance(location.getDeclaringClass()),
          methodVisitorContext);
    } catch (IllegalAccessException | InvocationTargetException exception) {
      throw new ServiceNotFoundException("error while discovering service: " + location.getDeclaringClass().getName(), exception);
    }

    this.methodVisitorContexts.add(methodVisitorContext);
  }
}
