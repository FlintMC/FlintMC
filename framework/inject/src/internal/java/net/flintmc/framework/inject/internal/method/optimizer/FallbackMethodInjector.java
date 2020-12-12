package net.flintmc.framework.inject.internal.method.optimizer;

import com.google.inject.Key;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.method.InjectedInvocationHelper;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FallbackMethodInjector
    implements InternalMethodInjector, MethodInjector {

  private final Logger logger;
  private final InjectedInvocationHelper invocationHelper;
  private final Method method;
  private Object instance;

  @AssistedInject
  private FallbackMethodInjector(
      @InjectLogger Logger logger,
      InjectedInvocationHelper invocationHelper,
      @Assisted Method method) {
    this.logger = logger;
    this.invocationHelper = invocationHelper;
    this.method = method;
  }

  @Override
  public Object getInstance() {
    if (this.instance == null) {
      this.instance = InjectionHolder.getInjectedInstance(this.method.getDeclaringClass());
    }

    return this.instance;
  }

  @Override
  public Object invoke(Map<Key<?>, ?> availableArguments) {
    try {
      return this.invocationHelper.invokeMethod(
          this.method, this.getInstance(), availableArguments);
    } catch (InvocationTargetException | IllegalAccessException exception) {
      this.logger.error(
          String.format(
              "Failed to invoke method %s.%s using fallback injector",
              this.method.getDeclaringClass().getName(), this.method.getName()),
          exception);
      return null;
    }
  }

  @Override
  public void init() {}

  @Override
  public void setMethod(Method method) {}

  @Override
  public void setInstance(Object instance) {
    this.instance = instance;
  }

  @AssistedFactory(FallbackMethodInjector.class)
  public interface BackupFactory {
    FallbackMethodInjector create(@Assisted Method method);
  }
}
