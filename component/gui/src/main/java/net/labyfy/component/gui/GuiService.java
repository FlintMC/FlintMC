package net.labyfy.component.gui;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.resolve.NameResolver;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.adapter.GuiAdapterProvider;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.invoke.InjectedInvocationHelper;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.hook.Hook;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Service(Gui.class)
public class GuiService implements ServiceHandler {

  private final GuiAdapterProvider guiAdapterProvider;
  private final InjectedInvocationHelper injectedInvocationHelper;
  private final ClassMappingProvider classMappingProvider;
  private final Collection<Identifier.Base> properties;

  @Inject
  private GuiService(
      GuiAdapterProvider guiAdapterProvider,
      InjectedInvocationHelper injectedInvocationHelper,
      ClassMappingProvider classMappingProvider) {
    this.guiAdapterProvider = guiAdapterProvider;
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.classMappingProvider = classMappingProvider;
    this.properties = ConcurrentHashMap.newKeySet();
  }

  public void discover(Identifier.Base property) {
    this.properties.add(property);
  }

  public void notify(
      Hook.ExecutionTime executionTime,
      GuiRenderState.Type targetGuiRenderState,
      Object screen,
      Map<String, Object> args) {

    for (Identifier.Base property : this.properties) {
      Gui gui = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
      NameResolver nameResolver = InjectionHolder.getInjectedInstance(gui.nameResolver());
      String resolve = nameResolver.resolve(gui.value());

      ClassMapping classMapping = classMappingProvider.get(screen.getClass().getName());

      if (classMapping == null
          || !(classMapping.getUnObfuscatedName().equals(resolve)
              || classMapping.getObfuscatedName().equals(resolve))) continue;

      for (Property.Base subProperty :
          property.getProperty().getSubProperties(GuiRenderState.class)) {
        GuiRenderState guiRenderState =
            subProperty.getLocatedIdentifiedAnnotation().getAnnotation();

        if (!guiRenderState.value().equals(targetGuiRenderState)
            || !guiRenderState.executionTime().equals(executionTime)) continue;
        Map<Key<?>, Object> parameters = Maps.newHashMap();

        GuiAdapter adapter = this.guiAdapterProvider.getAdapter(screen);

        parameters.put(Key.get(Object.class, Names.named("instance")), screen);
        parameters.put(Key.get(GuiRenderState.class), guiRenderState);
        parameters.put(Key.get(GuiAdapter.class), adapter);

        if (targetGuiRenderState == GuiRenderState.Type.RENDER) {
          adapter.updateMousePosition((int) args.get("mouseX"), (int) args.get("mouseY"));
          adapter.updatePartialTick((float) args.get("partialTick"));
        } else if (executionTime == Hook.ExecutionTime.BEFORE) {
          adapter.reset();
        }

        this.injectedInvocationHelper.invokeMethod(
            subProperty.getLocatedIdentifiedAnnotation().getLocation(), parameters);
      }
    }
  }
}
