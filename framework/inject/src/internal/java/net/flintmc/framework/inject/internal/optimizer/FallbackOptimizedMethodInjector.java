package net.flintmc.framework.inject.internal.optimizer;

import com.google.inject.Key;
import net.flintmc.framework.inject.InjectedInvocationHelper;
import net.flintmc.framework.inject.OptimizedMethodInjector;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.primitive.InjectionHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FallbackOptimizedMethodInjector
    implements InternalOptimizedMethodInjector, OptimizedMethodInjector {

  private final InjectedInvocationHelper invocationHelper;
  private final Method method;
  private Object instance;

  @AssistedInject
  private FallbackOptimizedMethodInjector(
      InjectedInvocationHelper invocationHelper, @Assisted Method method) {
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
      return this.invocationHelper.invokeMethod(this.method, this.getInstance(), availableArguments);
    } catch (InvocationTargetException | IllegalAccessException exception) {
      exception.printStackTrace(); // TODO replace with logger call
      return null;
    }
  }

  @Override
  public void init(Method method) {
  }

  @Override
  public void setInstance(Object instance) {
    this.instance = instance;
  }

  @AssistedFactory(FallbackOptimizedMethodInjector.class)
  public interface BackupFactory {
    FallbackOptimizedMethodInjector create(@Assisted Method method);
  }
}
