package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocation;

import java.util.Collection;

public interface RenderType {

  float ALPHA_DEFAULT = 1f / 255f;
  float ALPHA_HALF = 0.5f;
  float ALPHA_ZERO = 0f;
  float ALPHA_ONE = 1f;

  String getName();

  VertexFormat getFormat();

  int getDrawMode();

  RenderType blendFunc(int src, int dest);

  RenderType blend(boolean enabled);

  RenderType texture(ResourceLocation resourceLocation, boolean blur, boolean mipmap);

  RenderType transparency(Runnable enable, Runnable disable);

  RenderType diffuseLighting(boolean diffuseLighting);

  RenderType shadeModel(boolean shadeModel);

  RenderType alpha(float alpha);

  RenderType depthTest(int depthTest);

  RenderType cull(boolean cull);

  RenderType lightmap(boolean light);

  RenderType overlay(boolean overlay);

  RenderType fog(Runnable enable, Runnable disable);

  RenderType layer(Runnable enable, Runnable disable);

  RenderType target(Runnable enable, Runnable disable);

  RenderType texturing(Runnable enable, Runnable disable);

  RenderType writeMask(boolean colorMask, boolean depthMask);

  RenderType line(double line);

  RenderType lineEmpty();

  RenderType custom(RenderState renderState);

  boolean hasCustom(String name);

  Collection<RenderState> getCustomStates();

  <T> T getHandle();

  @AssistedFactory(RenderType.class)
  interface Factory {
    RenderType create(@Assisted VertexFormat format, @Assisted String name, @Assisted int drawMode);
  }

}
