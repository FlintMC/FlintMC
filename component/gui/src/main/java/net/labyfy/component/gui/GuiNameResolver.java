package net.labyfy.component.gui;

import net.labyfy.base.structure.resolve.NameResolver;

@FunctionalInterface
public interface GuiNameResolver extends NameResolver {

  String resolve(String name);
}
