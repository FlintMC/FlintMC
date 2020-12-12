package net.flintmc.framework.inject.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Implement(InjectionUtils.class)
public class DefaultInjectionUtils implements InjectionUtils {

  private final Logger logger;
  private final AtomicInteger idCounter;
  private final Random random;

  @Inject
  private DefaultInjectionUtils(@InjectLogger Logger logger) {
    this.logger = logger;
    this.idCounter = new AtomicInteger();
    this.random = new Random();
  }

  @Override
  public String generateInjectedFieldName() {
    return "injected_" + this.idCounter.getAndIncrement() + "_" + this.random.nextInt(99999);
  }

  @Override
  public CtField addInjectedField(
      CtClass declaringClass,
      String fieldName,
      String injectedTypeName,
      boolean singletonInstance,
      boolean staticField)
      throws CannotCompileException {
    if (declaringClass.isInterface()) {
      throw new IllegalArgumentException(
          "Cannot add fields to an interface: " + declaringClass.getName());
    }

    if (!singletonInstance) {
      for (CtField field : declaringClass.getDeclaredFields()) {
        try {
          if (!field.getType().getName().equals(injectedTypeName)) {
            continue;
          }
        } catch (NotFoundException exception) {
          this.logger.trace(
              String.format(
                  "Failed to find type of field %s.%s", declaringClass.getName(), field.getName()),
              exception);
          continue;
        }

        if (Modifier.isStatic(field.getModifiers()) == staticField) {
          return field;
        }
      }
    }

    CtField field =
        CtField.make(
            String.format(
                "private %s final %s %s = (%2$s) %s.getInjectedInstance(%2$s.class);",
                staticField ? "static" : "",
                injectedTypeName,
                fieldName,
                InjectionHolder.class.getName()),
            declaringClass);

    declaringClass.addField(field);

    return field;
  }
}
