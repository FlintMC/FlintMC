package net.labyfy.component.gui.mcjfxgl;

import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class McJfxGLSceneRepository {

  private final McJfxGLScene.Factory factory;
  private final Map<String, McJfxGLScene> scenes;

  @Inject
  private McJfxGLSceneRepository(McJfxGLScene.Factory factory) {
    this.factory = factory;
    this.scenes = Maps.newHashMap();
  }

  public synchronized McJfxGLScene get(String name) {
    return this.scenes.get(name);
  }

  public synchronized McJfxGLScene createOrGet(String name, Function<McJfxGLScene.Factory, McJfxGLScene> function) {
    this.scenes.computeIfAbsent(name, (s) -> function.apply(this.factory));
    return this.scenes.get(name);
  }
}
