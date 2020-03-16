package net.labyfy.component.transform.minecraft;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.Collection;

@Singleton
@Service(MinecraftTransformer.class)
public class MinecraftTransformerService implements ServiceHandler {

  private final Injector injector;
  private final Collection<IClassTransformer> classTransformers;

  @Inject
  private MinecraftTransformerService(Injector injector)
      throws NoSuchFieldException, IllegalAccessException {
    this.injector = injector;
    this.classTransformers = this.getClassTransformers();
  }

  private Collection<IClassTransformer> getClassTransformers()
      throws NoSuchFieldException, IllegalAccessException {
    Field transformers = LaunchClassLoader.class.getDeclaredField("transformers");
    transformers.setAccessible(true);
    return (Collection<IClassTransformer>) transformers.get(Launch.classLoader);
  }

  public void discover(Identifier.Base property) {
    this.classTransformers.add(
        this.injector.getInstance(
            property
                .getLocatedIdentifiedAnnotation()
                .<Class<? extends IClassTransformer>>getLocation()));
  }
}
