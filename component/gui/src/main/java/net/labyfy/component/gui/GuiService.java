package net.labyfy.component.gui;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Service(Gui.class)
public class GuiService implements ServiceHandler {

  private final InjectedInvocationHelper injectedInvocationHelper;
  private final ClassMappingProvider classMappingProvider;
  private final Collection<Identifier.Base> properties;
  private final GuiNameResolver guiNameResolver;
  private final GuiMethodResolver guiMethodResolver;
  private final Collection<String> modifiedGuis;

  @Inject
  private GuiService(
      InjectedInvocationHelper injectedInvocationHelper,
      ClassMappingProvider classMappingProvider,
      GuiNameResolver guiNameResolver,
      GuiMethodResolver guiMethodResolver) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.classMappingProvider = classMappingProvider;
    this.guiNameResolver = guiNameResolver;
    this.guiMethodResolver = guiMethodResolver;
    this.properties = ConcurrentHashMap.newKeySet();
    this.modifiedGuis = ConcurrentHashMap.newKeySet();
  }

  @ClassTransform
  @CtClassFilter(
      value = CtClassFilters.SUBCLASS_OF,
      className = Guis.GUI_CLASS,
      classNameResolver = GuiNameResolver.class)
  public void transform(ClassTransformContext classTransformContext) throws CannotCompileException {
    for (Identifier.Base property : this.properties) {
      if (guiNameResolver
          .resolve(
              property.getProperty().getLocatedIdentifiedAnnotation().<Gui>getAnnotation().value())
          .equals(classTransformContext.getCtClass().getName())) {
        if (this.modifiedGuis.contains(classTransformContext.getCtClass().getName())) return;
        this.modifiedGuis.add(classTransformContext.getCtClass().getName());

        for (GuiRenderState.Type type : GuiRenderState.Type.values()) {
          addHook(classTransformContext, type);
        }
      }
    }
  }

  private void addHook(ClassTransformContext classTransformContext, GuiRenderState.Type renderState)
      throws CannotCompileException {

    CtMethod initMethod =
        this.guiMethodResolver.resolve(classTransformContext.getCtClass(), renderState);

    initMethod
        .getDeclaringClass()
        .getClassPool()
        .importPackage(GuiService.class.getPackage().getName());
    initMethod.insertAfter("GuiService.HookReceiver.notify(this, \"" + renderState.name() + "\");");
  }

  public void discover(Identifier.Base property) {
    this.properties.add(property);
  }

  public void notify(Object gui, String state)
      throws InvocationTargetException, IllegalAccessException {

    for (Identifier.Base property : this.properties) {
      Gui annotation = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
      ClassMapping classMapping =
          this.classMappingProvider.get(guiNameResolver.resolve(annotation.value()));
      if (classMapping.getUnObfuscatedName().equals(gui.getClass().getName())) {
        for (Property.Base subProperty :
            property.getProperty().getSubProperties(GuiRenderState.class)) {

          if (subProperty
              .getLocatedIdentifiedAnnotation()
              .<GuiRenderState>getAnnotation()
              .value()
              .name()
              .equals(state)) {

            this.injectedInvocationHelper.invokeMethod(
                subProperty.getLocatedIdentifiedAnnotation().getLocation(),
                InjectionHolder.getInstance()
                    .getInjector()
                    .getInstance(
                        subProperty
                            .getLocatedIdentifiedAnnotation()
                            .<Method>getLocation()
                            .getDeclaringClass()),
                ImmutableMap.of());
          }
        }
      }
    }
  }

  public static class HookReceiver {

    private HookReceiver() {}

    public static void notify(Object gui, String state)
        throws InvocationTargetException, IllegalAccessException {
      InjectionHolder.getInjectedInstance(GuiService.class).notify(gui, state);
    }
  }
}
