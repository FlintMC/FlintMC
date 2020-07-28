package net.labyfy.internal.component.render.v1_15_2;

import net.labyfy.component.render.RenderType;
import net.labyfy.component.render.VertexFormat;
import net.labyfy.component.resources.ResourceLocation;

import java.util.OptionalDouble;

public class RenderTypeImpl implements RenderType {

  private final net.minecraft.client.renderer.RenderType.State.Builder stateBuilder;
  private final VertexFormat format;
  private final String name;
  private final int drawMode;

  public RenderTypeImpl(VertexFormat format, String name, int drawMode) {
    this.format = format;
    this.name = name;
    this.drawMode = drawMode;
    this.stateBuilder = net.minecraft.client.renderer.RenderType.State.getBuilder();
  }

  public RenderTypeImpl texture(ResourceLocation resourceLocation, boolean blur, boolean mipmap) {
    stateBuilder.texture(new net.minecraft.client.renderer.RenderState.TextureState(resourceLocation.getHandle(), blur, mipmap));
    return this;
  }

  public RenderTypeImpl transparency(Runnable enable, Runnable disable) {
    stateBuilder.transparency(new net.minecraft.client.renderer.RenderState.TransparencyState("transparency", enable, disable));
    return this;
  }

  public RenderTypeImpl diffuseLighting(boolean diffuseLighting) {
    stateBuilder.diffuseLighting(new net.minecraft.client.renderer.RenderState.DiffuseLightingState(diffuseLighting));
    return this;
  }

  public RenderTypeImpl shadeModel(boolean shadeModel) {
    stateBuilder.shadeModel(new net.minecraft.client.renderer.RenderState.ShadeModelState(shadeModel));
    return this;
  }

  public RenderTypeImpl alpha(float alpha) {
    stateBuilder.alpha(new net.minecraft.client.renderer.RenderState.AlphaState(alpha));
    return this;
  }

  public RenderTypeImpl depthTest(int depthTest) {
    stateBuilder.depthTest(new net.minecraft.client.renderer.RenderState.DepthTestState(depthTest));
    return this;
  }

  public RenderTypeImpl cull(boolean cull) {
    stateBuilder.cull(new net.minecraft.client.renderer.RenderState.CullState(cull));
    return this;
  }

  public RenderTypeImpl lightmap(boolean light) {
    stateBuilder.lightmap(new net.minecraft.client.renderer.RenderState.LightmapState(light));
    return this;
  }

  public RenderTypeImpl overlay(boolean overlay) {
    stateBuilder.overlay(new net.minecraft.client.renderer.RenderState.OverlayState(overlay));
    return this;
  }

  public RenderTypeImpl fog(Runnable enable, Runnable disable) {
    stateBuilder.fog(new net.minecraft.client.renderer.RenderState.FogState("fog", enable, disable));
    return this;
  }

  public RenderTypeImpl layer(Runnable enable, Runnable disable) {
    stateBuilder.layer(new net.minecraft.client.renderer.RenderState.LayerState("layer", enable, disable));
    return this;
  }

  public RenderTypeImpl target(Runnable enable, Runnable disable) {
    stateBuilder.target(new net.minecraft.client.renderer.RenderState.TargetState("target", enable, disable));
    return this;
  }

  public RenderTypeImpl texturing(Runnable enable, Runnable disable) {
    stateBuilder.texturing(new net.minecraft.client.renderer.RenderState.TexturingState("texturing", enable, disable));
    return this;
  }

  public RenderTypeImpl writeMask(boolean colorMask, boolean depthMask) {
    stateBuilder.writeMask(new net.minecraft.client.renderer.RenderState.WriteMaskState(colorMask, depthMask));
    return this;
  }

  public RenderTypeImpl line(double line) {
    stateBuilder.line(new net.minecraft.client.renderer.RenderState.LineState(OptionalDouble.of(line)));
    return this;
  }

  public RenderTypeImpl lineEmpty() {
    stateBuilder.line(new net.minecraft.client.renderer.RenderState.LineState(OptionalDouble.empty()));
    return this;
  }

  public <T> T getHandle() {
    return (T) net.minecraft.client.renderer.RenderType.makeType(this.name, this.getFormat().getHandle(), this.getDrawMode(), 0, this.stateBuilder.build(false));
  }

  public String getName() {
    return this.name;
  }

  public VertexFormat getFormat() {
    return this.format;
  }

  public int getDrawMode() {
    return this.drawMode;
  }

}
