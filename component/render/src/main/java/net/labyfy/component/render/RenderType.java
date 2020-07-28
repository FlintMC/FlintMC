package net.labyfy.component.render;

import net.labyfy.component.resources.ResourceLocation;

public interface RenderType {

  String getName();

  VertexFormat getFormat();

  int getDrawMode();

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

  <T> T getHandle();

}
