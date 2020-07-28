package net.labyfy.internal.component.render.v1_15_2;

import net.labyfy.component.render.VertexFormatElement;

public class VertexFormatElementImpl implements VertexFormatElement {

  private final String name;
  private final Type type;
  private final int amount;
  private final net.minecraft.client.renderer.vertex.VertexFormatElement handle;

  public VertexFormatElementImpl(String name, Type type, int amount) {
    this.name = name;
    this.type = type;
    this.amount = amount;
    this.handle = this.createHandle();
  }

  private net.minecraft.client.renderer.vertex.VertexFormatElement createHandle() {
    return new net.minecraft.client.renderer.vertex.VertexFormatElement(
        0,
        net.minecraft.client.renderer.vertex.VertexFormatElement.Type.valueOf(this.type.name()),
        this.findUsage(),
        amount);
  }

  private net.minecraft.client.renderer.vertex.VertexFormatElement.Usage findUsage() {
    for (net.minecraft.client.renderer.vertex.VertexFormatElement.Usage value : net.minecraft.client.renderer.vertex.VertexFormatElement.Usage.values()) {
      if (value.name().equals(this.name.toUpperCase())) {
        return value;
      }
    }
    return net.minecraft.client.renderer.vertex.VertexFormatElement.Usage.GENERIC;
  }

  public String getName() {
    return this.name;
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
}
