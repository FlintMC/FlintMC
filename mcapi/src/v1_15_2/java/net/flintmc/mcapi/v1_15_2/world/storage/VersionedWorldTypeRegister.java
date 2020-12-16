package net.flintmc.mcapi.v1_15_2.world.storage;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import java.util.List;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegister;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;

@Implement(value = WorldTypeRegister.class, version = "1.15.2")
public class VersionedWorldTypeRegister implements WorldTypeRegister {

  private final WorldMapper worldMapper;
  private final List<WorldType> worldTypes;

  @Inject
  private VersionedWorldTypeRegister(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldTypes = Lists.newArrayList();
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void loadWorldTypes(OpenGLInitializeEvent event) {
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
