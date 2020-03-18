package net.labyfy.component.gui;

import javassist.CtClass;
import javassist.CtMethod;

public interface GuiMethodResolver {
  CtMethod resolve(CtClass ctClass, GuiRenderState.Type renderState);
}
