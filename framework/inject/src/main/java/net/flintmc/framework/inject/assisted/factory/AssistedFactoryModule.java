package net.flintmc.framework.inject.assisted.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Providers;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import net.flintmc.framework.inject.assisted.data.AssistData;

/**
 * The module configures whether the method should create the constructor for the module.
 */
public class AssistedFactoryModule extends AbstractModule {

  private final Method method;
  private final Object[] arguments;
  private final AssistData assistData;
  private final Key<?> returnKey;

  /**
   * Constructs a new {@link AssistedFactoryModule} with the given parameters.
   *
   * @param method     The method that should be bound to the {@link Binder#withSource(Object)}
   * @param arguments  The arguments of the method.
   * @param assistData The assisted data for the module.
   * @param returnKey  The return type of the method.
   */
  public AssistedFactoryModule(Method method, Object[] arguments, AssistData assistData,
      Key<?> returnKey) {
    this.method = method;
    this.arguments = arguments;
    this.assistData = assistData;
    this.returnKey = returnKey;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected void configure() {
    Binder binder = binder().withSource(method);

    int providerCount = 0;
    boolean optimized = assistData.isOptimized();

    for (Key<?> parameterType : assistData.getParameterTypes()) {
      binder.bind((Key) parameterType)
          .toProvider(
              optimized ?
                  assistData.getProviders().get(providerCount++) :
                  Providers.of(arguments[providerCount++])
          );
    }

    // Retrieves the constructor of the assisted data
    Constructor constructor = assistData.getConstructor();

    if (constructor != null) {
      binder.bind(returnKey)
          .toConstructor(constructor, (TypeLiteral) assistData.getImplementationType())
          .in(Scopes.NO_SCOPE);
    }
  }
}
