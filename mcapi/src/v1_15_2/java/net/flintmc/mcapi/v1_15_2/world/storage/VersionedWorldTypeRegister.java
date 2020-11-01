package net.flintmc.mcapi.v1_15_2.world.storage;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.mcapi.world.storage.WorldType;
import net.flintmc.mcapi.world.storage.WorldTypeRegister;

import java.util.List;

@Implement(value = WorldTypeRegister.class, version = "1.15.2")
public class VersionedWorldTypeRegister implements WorldTypeRegister {

  private final WorldType.Factory worldTypeFactory;
  private final List<WorldType> worldTypes;

  @Inject
  public VersionedWorldTypeRegister(WorldType.Factory worldTypeFactory) {
    this.worldTypeFactory = worldTypeFactory;
    this.worldTypes = Lists.newArrayList();
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void loadWorldTypes() {
    for (net.minecraft.world.WorldType worldType : net.minecraft.world.WorldType.WORLD_TYPES) {
      if (worldType != null) {
        WorldType type = this.worldTypeFactory.create(
                worldType.getId(),
                worldType.getName(),
                worldType.getSerialization(),
                worldType.getVersion(),
                worldType.canBeCreated(),
                worldType.isVersioned(),
                worldType.hasInfoNotice(),
                worldType.hasCustomOptions()
        );
        this.worldTypes.add(type);
      }
    }
  }

  @Override
  public List<WorldType> getWorldTypes() {
    return this.worldTypes;
  }
}
