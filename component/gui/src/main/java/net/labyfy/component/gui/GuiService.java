package net.labyfy.component.gui;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;

import java.util.Collection;

@Singleton
@Service(Gui.class)
public class GuiService implements ServiceHandler {

  private final Collection<Identifier.Base> properties;
  private final GuiNameResolver guiNameResolver;
  private final GuiMethodResolver guiMethodResolver;

  @Inject
  private GuiService(GuiNameResolver guiNameResolver, GuiMethodResolver guiMethodResolver) {
    this.guiNameResolver = guiNameResolver;
    this.guiMethodResolver = guiMethodResolver;
    this.properties = Sets.newHashSet();
  }

  @ClassTransform
  @CtClassFilter(
      value = CtClassFilters.SUBCLASS_OF,
      className = Guis.GUI_CLASS,
      classNameResolver = GuiNameResolver.class)
  public void transform(ClassTransformContext classTransformContext) {
    for (Identifier.Base property : this.properties) {
      if (guiNameResolver
          .resolve(
              property.getProperty().getLocatedIdentifiedAnnotation().<Gui>getAnnotation().value())
          .equals(classTransformContext.getCtClass().getName())) {
        Collection<Property.Base> renderStates =
            property.getProperty().getSubProperties(GuiRenderState.class);
        for (Property.Base renderState : renderStates) {
          GuiRenderState.Type value =
              renderState.getLocatedIdentifiedAnnotation().<GuiRenderState>getAnnotation().value();
          switch (value) {
            case INIT:
              this.addInitHook(classTransformContext, value);
              break;
            case RENDER:
              break;
          }
        }
      }
    }
  }

  private void addInitHook(
      ClassTransformContext classTransformContext, GuiRenderState.Type renderState) {
    System.out.println(
        "Methooooood "
            + this.guiMethodResolver.resolve(classTransformContext.getCtClass(), renderState));
    //    classTransformContext.getCtClass().
  }

  public void discover(Identifier.Base property) {
    this.properties.add(property);
  }
}
