package net.labyfy.component.resources.pack;

import net.labyfy.component.resources.ResourceLocation;

import java.io.InputStream;
import java.util.Collection;

public interface ResourcePack {

  Collection<String> getNameSpaces();

  String getName();

  InputStream getStream(ResourceLocation resourceLocation);

  String getDescription();

  String getTitle();
}
