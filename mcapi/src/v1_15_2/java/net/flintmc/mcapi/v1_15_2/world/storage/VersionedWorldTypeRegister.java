package net.flintmc.mcapi.v1_15_2.world.storage;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegister;

import java.util.List;

@Implement(value = WorldTypeRegister.class, version = "1.15.2")
public class VersionedWorldTypeRegister implements WorldTypeRegister {

  private final WorldMapper worldMapper;
  private final List<WorldType> worldTypes;

  @Inject
  private VersionedWorldTypeRegister(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldTypes = Lists.newArrayList();
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void loadWorldTypes() {
    for (net.minecraft.world.WorldType worldType : net.minecraft.world.WorldType.WORLD_TYPES) {
      if (worldType != null) {
        this.worldTypes.add(this.worldMapper.fromMinecraftWorldType(worldType));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WorldType> getWorldTypes() {
    return this.worldTypes;
  }
}
