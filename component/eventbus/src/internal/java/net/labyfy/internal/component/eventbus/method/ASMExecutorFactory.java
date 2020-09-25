package net.labyfy.internal.component.eventbus.method;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.Type;
import net.labyfy.component.eventbus.method.Executor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.Opcodes.*;

/**
 * An executor factory which used ASM to create event executors.
 */
public class ASMExecutorFactory implements Executor.Factory {

  private static final String[] GENERATED_EVENT_EXECUTOR_NAME = new String[]{Type.getInternalName(Executor.class)};

  private final String session = UUID.randomUUID().toString().replace("-", "");
  private final AtomicInteger identifier = new AtomicInteger();

  private final ExecutorClassLoader eventExecutorClassLoader;
  private final LoadingCache<Method, Class<? extends Executor>> cache;

  public ASMExecutorFactory() {
    this(ASMExecutorFactory.class.getClassLoader());
  }

  public ASMExecutorFactory(ClassLoader parent) {
    this.eventExecutorClassLoader = new ExecutorClassLoader(parent);
    this.cache = CacheBuilder.newBuilder()
            .initialCapacity(16)
            .weakValues()
            .build(new CacheLoader<Method, Class<? extends Executor>>() {
              @Override
              public Class<? extends Executor> load(Method method) throws Exception {
                Objects.requireNonNull(method, "method");

                Class<?> listener = method.getDeclaringClass();
                String listenerName = Type.getInternalName(listener);

                Class<?> parameter = method.getParameterTypes()[0];
                String className = executorClassName(listener, method, parameter);

                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                classWriter.visit(
                        V1_8,
                        ACC_PUBLIC | ACC_FINAL,
                        className.replace('.', '/'),
                        null,
                        "java/lang/Object",
                        GENERATED_EVENT_EXECUTOR_NAME
                );

                MethodVisitor methodVisitor;

                // Visits a constructor to the class
                methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                methodVisitor.visitInsn(RETURN);
                methodVisitor.visitMaxs(0, 0);
                methodVisitor.visitEnd();

                // Visits a method [invoke(Object, Object)]
                methodVisitor = classWriter.visitMethod(
                        ACC_PUBLIC,
                        "invoke",
                        "(Ljava/lang/Object;Ljava/lang/Object;)V",
                        null,
                        null
                );
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitTypeInsn(CHECKCAST, listenerName);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitTypeInsn(CHECKCAST, Type.getInternalName(parameter));
                methodVisitor.visitMethodInsn(
                        INVOKEVIRTUAL,
                        listenerName,
                        method.getName(),
                        Type.getMethodDescriptor(method),
                        false
                );
                methodVisitor.visitInsn(RETURN);
                methodVisitor.visitMaxs(0, 0);
                methodVisitor.visitEnd();

                classWriter.visitEnd();

                return eventExecutorClassLoader.defineClass(className, classWriter.toByteArray());
              }
            });
  }

  /**
   * Retrieves the formatted {@link Executor} class name.
   *
   * @param listener  The class of the listener.
   * @param method    The subscribed method.
   * @param parameter The first parameter of the subscribed method.
   * @return The formatted {@link Executor} class name.
   */
  private String executorClassName(final Class<?> listener, final Method method, final Class<?> parameter) {
    return String.format(
            "%s.%s.%s-%s-%s-%d",
            "net.labyfy.component.event.asm.generated",
            this.session,
            listener.getSimpleName(),
            method.getName(),
            parameter.getSimpleName(),
            this.identifier.incrementAndGet()
    );
  }

  /**
   * {@inheritDoc}
   *
   * @param listener The listener object
   * @param method   The method to call on the object.
   * @return
   * @throws IllegalAccessException If the class or its nullary constructor is not accessible.
   * @throws InstantiationException If this {@link Class} represents an abstract class, an interface, an array
   *                                class, a primitive type, or void; or if the class has not nullary constructor;
   *                                or if the instantiation fails or some other reason.
   */
  @Override
  public Executor create(Object listener, Method method) throws IllegalAccessException, InstantiationException {
    if (!Modifier.isPublic(listener.getClass().getModifiers())) {
      throw new IllegalArgumentException(
              String.format("Listener class '%s' must be public", listener.getClass().getName())
      );
    }
    if (!Modifier.isPublic(method.getModifiers())) {
      throw new IllegalArgumentException(String.format("Subscriber method '%s' must be public", method));
    }
    return this.cache.getUnchecked(method).newInstance();
  }

}
