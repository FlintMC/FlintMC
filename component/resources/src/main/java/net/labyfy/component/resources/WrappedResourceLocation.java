package net.labyfy.component.resources;

import com.google.inject.assistedinject.Assisted;

import java.io.InputStream;

public class WrappedResourceLocation implements ResourceLocation {

  private final ResourceLocation resourceLocation;

  protected WrappedResourceLocation(ResourceLocation resourceLocation) {
    this.resourceLocation = resourceLocation;
  }

  public <T> T getHandle() {
    return this.resourceLocation.getHandle();
  }

  public String getPath() {
    return this.resourceLocation.getPath();
  }

  public String getNamespace() {
    return this.resourceLocation.getNamespace();
  }

  public InputStream openInputStream() {
    return this.resourceLocation.openInputStream();
  }

  public <T extends WrappedResourceLocation> T as(Class<T> clazz) {
    return this.resourceLocation.as(clazz);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || (getClass() != o.getClass() && this.resourceLocation.getClass() != o.getClass())) return false;
    WrappedResourceLocation that = (WrappedResourceLocation) o;
    return this.resourceLocation.equals(that.resourceLocation);
  }

  public int hashCode() {
    return this.resourceLocation.hashCode();
  }

}
