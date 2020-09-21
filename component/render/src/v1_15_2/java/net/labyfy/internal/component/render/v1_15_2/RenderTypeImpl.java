package net.labyfy.internal.component.render.v1_15_2;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.RenderType;
import net.labyfy.component.render.VertexFormat;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderState;

import java.lang.reflect.Field;
import java.util.*;

@Implement(RenderType.class)
public class RenderTypeImpl implements RenderType {

  private final Collection<net.labyfy.component.render.RenderState> customRenderStates = new HashSet<>();
  private final net.minecraft.client.renderer.RenderType.State.Builder stateBuilder;
  private final VertexFormat format;
  private final String name;
  private final int drawMode;
  private net.minecraft.client.renderer.RenderType handle;
  private boolean handleInvalid = false;

  @AssistedInject
  private RenderTypeImpl(@Assisted VertexFormat format, @Assisted String name, @Assisted int drawMode) {
    this.format = format;
    this.name = name;
    this.drawMode = drawMode;
    this.stateBuilder = net.minecraft.client.renderer.RenderType.State.getBuilder();

  }

  private net.minecraft.client.renderer.RenderType updateHandle() {
    net.minecraft.client.renderer.RenderType renderType = net.minecraft.client.renderer.RenderType.makeType(this.name, this.getFormat().getHandle(), this.getDrawMode(), 0, this.stateBuilder.build(false));
    try {
      Field renderStatesField = net.minecraft.client.renderer.RenderType.State.class.getDeclaredField("renderStates");
      renderStatesField.setAccessible(true);

      Field renderStateField = Class.forName("net.minecraft.client.renderer.RenderType$Type").getDeclaredField("renderState");
      renderStateField.setAccessible(true);
      Collection<net.minecraft.client.renderer.RenderState> renderStates = new ArrayList<>((Collection<RenderState>) renderStatesField.get(renderStateField.get(renderType)));

      for (net.labyfy.component.render.RenderState customRenderState : this.customRenderStates) {
        renderStates.add(new RenderState(customRenderState.getName(), customRenderState::enable, customRenderState::disable) {
        });
      }

      renderStatesField.set(renderStateField.get(renderType), ImmutableList.copyOf(renderStates));

    } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    handleInvalid = true;
    return this.handle = renderType;
  }

  public RenderTypeImpl blendFunc(int src, int dest) {
    return this.custom(new net.labyfy.component.render.RenderState() {
      public void enable() {
        RenderSystem.blendFunc(src, dest);
      }

      public void disable() {

      }

      public String getName() {
        return "blend_func";
      }
    });
  }

  public RenderType blend(boolean enabled) {
    if (!enabled) return this;
    return this.custom(new net.labyfy.component.render.RenderState() {
      public void enable() {
        RenderSystem.enableBlend();
      }

      public void disable() {
        RenderSystem.disableBlend();
      }

      public String getName() {
        return "blend";
      }
    });
  }

  public RenderTypeImpl texture(ResourceLocation resourceLocation, boolean blur, boolean mipmap) {
    stateBuilder.texture(new net.minecraft.client.renderer.RenderState.TextureState(resourceLocation.getHandle(), blur, mipmap));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl transparency(Runnable enable, Runnable disable) {
    stateBuilder.transparency(new net.minecraft.client.renderer.RenderState.TransparencyState("transparency", enable, disable));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl diffuseLighting(boolean diffuseLighting) {
    stateBuilder.diffuseLighting(new net.minecraft.client.renderer.RenderState.DiffuseLightingState(diffuseLighting));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl shadeModel(boolean shadeModel) {
    stateBuilder.shadeModel(new net.minecraft.client.renderer.RenderState.ShadeModelState(shadeModel));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl alpha(float alpha) {
    stateBuilder.alpha(new net.minecraft.client.renderer.RenderState.AlphaState(alpha));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl depthTest(int depthTest) {
    stateBuilder.depthTest(new net.minecraft.client.renderer.RenderState.DepthTestState(depthTest));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl cull(boolean cull) {
    stateBuilder.cull(new net.minecraft.client.renderer.RenderState.CullState(cull));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl lightmap(boolean light) {
    stateBuilder.lightmap(new net.minecraft.client.renderer.RenderState.LightmapState(light));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl overlay(boolean overlay) {
    stateBuilder.overlay(new net.minecraft.client.renderer.RenderState.OverlayState(overlay));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl fog(Runnable enable, Runnable disable) {
    stateBuilder.fog(new net.minecraft.client.renderer.RenderState.FogState("fog", enable, disable));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl layer(Runnable enable, Runnable disable) {
    stateBuilder.layer(new net.minecraft.client.renderer.RenderState.LayerState("layer", enable, disable));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl target(Runnable enable, Runnable disable) {
    stateBuilder.target(new net.minecraft.client.renderer.RenderState.TargetState("target", enable, disable));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl texturing(Runnable enable, Runnable disable) {
    stateBuilder.texturing(new net.minecraft.client.renderer.RenderState.TexturingState("texturing", enable, disable));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl writeMask(boolean colorMask, boolean depthMask) {
    stateBuilder.writeMask(new net.minecraft.client.renderer.RenderState.WriteMaskState(colorMask, depthMask));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl line(double line) {
    stateBuilder.line(new net.minecraft.client.renderer.RenderState.LineState(OptionalDouble.of(line)));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl lineEmpty() {
    stateBuilder.line(new net.minecraft.client.renderer.RenderState.LineState(OptionalDouble.empty()));
    handleInvalid = true;
    return this;
  }

  public RenderTypeImpl custom(net.labyfy.component.render.RenderState renderState) {
    this.customRenderStates.add(renderState);
    handleInvalid = true;
    return this;
  }

  public boolean hasCustom(String name) {
    for (net.labyfy.component.render.RenderState customRenderState : this.customRenderStates) {
      if (customRenderState.getName().equals(name))
        return true;
    }
    return false;
  }

  public Collection<net.labyfy.component.render.RenderState> getCustomStates() {
    return Collections.unmodifiableCollection(this.customRenderStates);
  }

  public <T> T getHandle() {
    if (this.handle == null) {
      this.updateHandle();
      stateBuilder.transparency(new net.minecraft.client.renderer.RenderState.TransparencyState("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
      }, () -> {
        RenderSystem.disableBlend();
      }));
      stateBuilder.alpha(new RenderState.AlphaState(RenderType.ALPHA_DEFAULT));
      return (T) this.handle;
    }
    if (this.handleInvalid) {
      this.updateHandle();
    }
    return (T) this.handle;
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
