package net.flintmc.framework.inject.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Implement(InjectionUtils.class)
public class DefaultInjectionUtils implements InjectionUtils {

  private final AtomicInteger idCounter;

  @Inject
  private DefaultInjectionUtils() {
    this.idCounter = new AtomicInteger();
  }

  @Override
  public String generateInjectedFieldName() {
    return "injected_" + this.idCounter.get() + "_" + UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, String injectedTypeName)
      throws CannotCompileException {
    String fieldName = this.generateInjectedFieldName();
    return this.addInjectedField(declaringClass, fieldName, injectedTypeName);
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, Class<?> injectedType)
      throws CannotCompileException {
    return this.addInjectedField(declaringClass, injectedType.getName());
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, CtClass injectedType)
      throws CannotCompileException {
    return this.addInjectedField(declaringClass, injectedType.getName());
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, String fieldName, String injectedTypeName)
      throws CannotCompileException {
    CtField field =
        CtField.make(
            String.format(
                "private static final %s %s = (%1$s) %s.getInjectedInstance(%1$s.class);",
                injectedTypeName, fieldName, InjectionHolder.class.getName()),
            declaringClass);

    declaringClass.addField(field);

    return field;
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, String fieldName, Class<?> injectedType)
      throws CannotCompileException {
    return this.addInjectedField(declaringClass, fieldName, injectedType.getName());
  }

  @Override
  public CtField addInjectedField(CtClass declaringClass, String fieldName, CtClass injectedType)
      throws CannotCompileException {
    return this.addInjectedField(declaringClass, fieldName, injectedType.getName());
  }
}
