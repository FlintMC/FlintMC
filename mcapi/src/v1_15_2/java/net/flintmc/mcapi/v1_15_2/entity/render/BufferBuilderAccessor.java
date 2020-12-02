package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.List;

@Shadow("net.minecraft.client.renderer.BufferBuilder")
public interface BufferBuilderAccessor {

  @FieldGetter("drawStates")
  List<BufferBuilder.DrawState> getDrawStates();

  @FieldGetter("vertexFormat")
  VertexFormat getVertexFormat();

}
