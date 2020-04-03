package net.labyfy.component.resources.pack;

import java.util.Collection;

public interface ResourcePack {

  Collection<String> getNameSpaces();

  String getName();

  String getDescription();

  String getTitle();
}
