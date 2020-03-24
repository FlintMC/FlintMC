package net.labyfy.component.gui;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.base.structure.resolve.AnnotationResolver;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.hook.HookFilter;
import net.labyfy.component.transform.hook.HookFilters;
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
  private final Collection<String> modifiedGuis;

  @Inject
  private GuiService(
          InjectedInvocationHelper injectedInvocationHelper,
          ClassMappingProvider classMappingProvider,
          GuiNameResolver guiNameResolver) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.classMappingProvider = classMappingProvider;
    this.guiNameResolver = guiNameResolver;
    this.properties = ConcurrentHashMap.newKeySet();
    this.modifiedGuis = ConcurrentHashMap.newKeySet();
  }

  public void discover(Identifier.Base property) {
    this.properties.add(property);
  }


}
