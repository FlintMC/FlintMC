package net.labyfy.component.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

@Singleton
@Service(WrapResourceLocation.class)
public class WrapResourceLocationService implements ServiceHandler {

  private final InjectedInvocationHelper injectedInvocationHelper;
  private final Map<Class<? extends WrappedResourceLocation>, Function<ResourceLocation, ? extends WrappedResourceLocation>> wrapper;

  @Inject
  private WrapResourceLocationService(InjectedInvocationHelper injectedInvocationHelper) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.wrapper = Maps.newHashMap();
  }

  public void discover(Identifier.Base property) {
    Method location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    if (WrappedResourceLocation.class.isAssignableFrom(location.getReturnType())) {
      this.wrapper.put((Class<? extends WrappedResourceLocation>) location.getReturnType(), resourceLocation -> this.injectedInvocationHelper.invokeMethod(location, ImmutableMap.of(Key.get(ResourceLocation.class), resourceLocation)));
    }
  }

  public <T extends ResourceLocation> T wrap(ResourceLocation resourceLocation, Class<T> clazz) {
    return (T) this.wrapper.get(clazz).apply(resourceLocation);
  }
}
