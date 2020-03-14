package net.labymod.component.transform.minecraft;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.List;

@Singleton
@Service(MinecraftTransformer.class)
public class MinecraftTransformerService implements ServiceHandler, IClassTransformer {

  public byte[] transform(String s, String s1, byte[] bytes) {
    return bytes;
  }

  public void discover(Identifier.Base property) {

  }

  @Task(value = Tasks.PRE_MINECRAFT_INITIALIZE, async = false)
  private void register() {
    try {
      System.out.println("Registered minecraft transformer service");
      Field transformersField = LaunchClassLoader.class.getDeclaredField("transformers");
      transformersField.setAccessible(true);
      List<IClassTransformer> transformers =
          (List<IClassTransformer>) transformersField.get(Launch.classLoader);
      transformers.add(this);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
