package net.labyfy.internal.component.render.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.VertexFormatElement;
import net.labyfy.component.render.VertexFormatElementType;
import net.labyfy.component.render.VertexFormatUsage;

@Implement(VertexFormatElement.class)
public class VertexFormatElementImpl implements VertexFormatElement {

  private final int index;
  private final VertexFormatUsage usage;
  private final Type type;
  private final int amount;
  private final net.minecraft.client.renderer.vertex.VertexFormatElement handle;
  private final VertexFormatElementType id;

  @AssistedInject
  private VertexFormatElementImpl(
      @Assisted("index") int index,
      @Assisted VertexFormatUsage usage,
      @Assisted Type type,
      @Assisted("amount") int amount,
      @Assisted("id") VertexFormatElementType id) {
    this.index = index;
    this.usage = usage;
    this.type = type;
    this.amount = amount;
    this.id = id;
    this.handle = this.createHandle();
  }

  private net.minecraft.client.renderer.vertex.VertexFormatElement createHandle() {
    return new net.minecraft.client.renderer.vertex.VertexFormatElement(
        this.index,
        net.minecraft.client.renderer.vertex.VertexFormatElement.Type.valueOf(this.type.name()),
        this.findUsage(),
        amount);
  }

  private net.minecraft.client.renderer.vertex.VertexFormatElement.Usage findUsage() {
    for (net.minecraft.client.renderer.vertex.VertexFormatElement.Usage value : net.minecraft.client.renderer.vertex.VertexFormatElement.Usage.values()) {
      if (value.name().equalsIgnoreCase(this.usage.name())) {
        return value;
      }
    }
    return net.minecraft.client.renderer.vertex.VertexFormatElement.Usage.GENERIC;
  }

  public int getIndex() {
    return this.index;
  }

  public <T> T getHandle() {
    return (T) this.handle;
  }

  public int getAmount() {
    return amount;
  }

  public Type getType() {
    return this.type;
  }

  public VertexFormatElementType getId() {
    return this.id;
  }
}
