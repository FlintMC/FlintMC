package net.labyfy.component.gui.mcjfxgl.component.theme;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;
import net.labyfy.component.inject.event.Event;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Singleton
public class ThemeRepository {

  private final ResourcePackProvider resourcePackProvider;
  private final ResourceLocationProvider resourceLocationProvider;
  private final Theme.Factory themeFactory;
  private Theme activeTheme;
  private boolean active = true;

  @Inject
  private ThemeRepository(
      ResourcePackProvider resourcePackProvider,
      ResourceLocationProvider resourceLocationProvider,
      Theme.Factory themeFactory) {
    this.resourcePackProvider = resourcePackProvider;
    this.resourceLocationProvider = resourceLocationProvider;
    this.themeFactory = themeFactory;
  }

  @Event(ResourcePackReloadEvent.class)
  public void reloadThemes() throws IOException {
    this.activeTheme = null;
    Map<String, byte[]> bytes = new HashMap<>();

    for (ResourceLocation labyfy : this.resourceLocationProvider.getLoaded("labyfy")) {
      bytes.put(
          labyfy.getPath().replaceFirst("labyfy/themes/", ""),
          IOUtils.toByteArray(labyfy.openInputStream()));
    }

    ClassPath.from(ClassLoader.getSystemClassLoader()).getResources().stream()
        .filter(
            resourceInfo ->
                resourceInfo.getResourceName().startsWith("assets/minecraft/labyfy/themes"))
        .map(ClassPath.ResourceInfo::getResourceName)
        .map(resource -> resource.replaceFirst("assets/minecraft/", ""))
        .forEach(
            resourceInfo -> {
              try {
                bytes.put(
                    resourceInfo.replaceFirst("labyfy/themes/", ""),
                    IOUtils.toByteArray(
                        this.resourceLocationProvider.get(resourceInfo).openInputStream()));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    System.out.println(bytes);

    if (!bytes.containsKey("theme.groovy")) return;

    ThemeConfig themeConfig =
        this.parseAndRun(bytes.get("theme.groovy"), ThemeConfig.Handle.class).toModel();

    Multimap<Class<? extends McJfxGLControl>, ThemeComponentStyle> stylesMultimap =
        HashMultimap.create();

    for (Map.Entry<String, byte[]> entry : bytes.entrySet()) {
      if (!entry.getKey().endsWith(".groovy") || entry.getKey().equals("theme.groovy")) continue;

      try {
        ThemeComponentStyle themeComponentStyle =
            this.parseAndRun(entry.getValue(), ThemeComponentStyle.Handle.class).toModel();
        stylesMultimap.put(themeComponentStyle.getTarget(), themeComponentStyle);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    for (Class<? extends McJfxGLControl> key : stylesMultimap.keys()) {
      Collection<ThemeComponentStyle> themeComponentStyles = stylesMultimap.get(key);
      if (themeComponentStyles.size() == 1) continue;
      stylesMultimap.removeAll(key);
      System.out.println("Remove invalid component style for " + key.getName());
    }

    this.activeTheme =
        this.themeFactory.create(
            bytes,
            themeConfig,
            stylesMultimap.entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  private <T extends Script> T parseAndRun(byte[] source, Class<? extends T> baseClass) {
    try {
      return this.parseAndRun(IOUtils.toString(source, "utf-8"), baseClass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private <T extends Script> T parseAndRun(String source, Class<? extends T> baseClass) {
    CompilerConfiguration compilerConfiguration = new CompilerConfiguration();

    compilerConfiguration.setScriptBaseClass(baseClass.getName());
    T handle = (T) new GroovyShell(compilerConfiguration).parse(source);

    handle.run();
    return handle;
  }

  public boolean isActive() {
    return active;
  }

  public ThemeRepository setActive(boolean active) {
    this.active = active;
    return this;
  }

  public Theme getActive() {
    return this.isActive() ? activeTheme : null;
  }
}
