package net.labyfy.component.resources;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.InputStream;

public interface ResourceLocation {

  /**
   * @return a nmc version of this {@link ResourceLocation}.
   */
  <T> T getHandle();

  String getPath();

  String getNamespace();

  InputStream openInputStream();

  <T extends WrappedResourceLocation> T as(Class<T> clazz);

  @AssistedFactory(ResourceLocation.class)
  interface Factory {
    ResourceLocation create(@Assisted("nameSpace") String nameSpace);

    ResourceLocation create(@Assisted("nameSpace") String nameSpace, @Assisted("path") String path);
  }
}
