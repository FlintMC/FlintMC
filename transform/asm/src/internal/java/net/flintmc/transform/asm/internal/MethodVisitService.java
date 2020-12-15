package net.flintmc.transform.asm.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.asm.MethodVisit;
import net.flintmc.transform.asm.MethodVisitorContext;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@MinecraftTransformer
@Service(MethodVisit.class)
public class MethodVisitService implements ServiceHandler<MethodVisit>, LateInjectedTransformer {

  private final Map<DefaultMethodVisitorContext, MethodVisitorInjector> methodVisitorContexts;
  private final Collection<DefaultMethodVisitorContext> registeredContexts;
  private final ClassMappingProvider classMappingProvider;
  private final MethodInjector.Factory injectorFactory;

  @Inject
  private MethodVisitService(
      ClassMappingProvider classMappingProvider, MethodInjector.Factory injectorFactory) {
    this.classMappingProvider = classMappingProvider;
    this.injectorFactory = injectorFactory;
    this.methodVisitorContexts = new ConcurrentHashMap<>();
    this.registeredContexts = new HashSet<>();
  }

  @Override
  public byte[] transform(String s, byte[] bytes) {
    ClassMapping classMapping = this.classMappingProvider.get(s);
    if (classMapping == null) {
      return bytes;
    }

    this.methodVisitorContexts.forEach(
        (methodVisitorContext, injector) -> {
          if (!this.registeredContexts.contains(methodVisitorContext)) {
            injector.notify(methodVisitorContext);
            this.registeredContexts.add(methodVisitorContext);
          }
        });

    for (MethodVisitorContext methodVisitorContext : this.methodVisitorContexts.keySet()) {
      MethodVisit methodVisit = methodVisitorContext.getMethodVisit();
      String className = methodVisit.className();

      if (!(className.equals(classMapping.getObfuscatedName())
          || className.equals(classMapping.getDeobfuscatedName()))) {
        continue;
      }

      ClassReader classReader = new ClassReader(bytes);
      ClassWriter classWriter = new ClassWriter(classReader, 3);

      ClassVisitor classVisitor =
          new ClassVisitor(Opcodes.ASM4, classWriter) {
            public MethodVisitor visitMethod(
                int access, String name, String desc, String signature, String[] exceptions) {

              MethodMapping methodMapping =
                  classMapping.getMethodByIdentifier(
                      name + desc.substring(0, desc.lastIndexOf(')') + 1));

              if (methodMapping != null) {
                for (DefaultMethodVisitorContext methodVisitorContext :
                    methodVisitorContexts.keySet()) {
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

  @Override
  public void discover(AnnotationMeta<MethodVisit> meta) {
    MethodVisitorInjector injector =
        this.injectorFactory.generate(
            meta.getMethodIdentifier().getLocation(), MethodVisitorInjector.class);

    this.methodVisitorContexts.put(new DefaultMethodVisitorContext(meta.getAnnotation()), injector);
  }
}
