package net.flintmc.mcapi.v1_15_2.tileentity.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.tileentity.type.TileEntityType;
import net.flintmc.mcapi.tileentity.type.TileEntityTypeRegister;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.minecraft.util.registry.Registry;

import java.util.Map;

@Singleton
@Implement(value = TileEntityTypeRegister.class, version = "1.15.2")
public class VersionedTileEntityTypeRegister implements TileEntityTypeRegister {

  private final Map<String, TileEntityType> tileEntityTypes;
  private final TileEntityType.Factory tileEntityTypeFactory;

  @Inject
  private VersionedTileEntityTypeRegister(TileEntityType.Factory tileEntityTypeFactory) {
    this.tileEntityTypeFactory = tileEntityTypeFactory;
    this.tileEntityTypes = Maps.newHashMap();
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void convertTileEntityTypes(OpenGLInitializeEvent event) {
    for (net.minecraft.tileentity.TileEntityType<?> tileEntityType : Registry.BLOCK_ENTITY_TYPE) {
      String key = Registry.BLOCK_ENTITY_TYPE.getKey(tileEntityType).getPath();
      this.tileEntityTypes.put(key, this.tileEntityTypeFactory.create());
    }
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, TileEntityType> getTileEntitiesTypes() {
    return this.tileEntityTypes;
  }

  /** {@inheritDoc} */
  @Override
  public TileEntityType getTileEntityType(String key) {
    return this.tileEntityTypes.get(key);
  }
}
