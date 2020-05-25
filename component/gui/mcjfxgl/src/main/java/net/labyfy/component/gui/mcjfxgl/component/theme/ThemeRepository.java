package net.labyfy.component.gui.mcjfxgl.component.theme;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.theme.source.ThemeGroovyCodeSourceShim;
import net.labyfy.component.gui.mcjfxgl.component.theme.source.ThemePermissionChecker;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;
import net.labyfy.component.inject.event.Event;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.security.LabyfySecurityManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Singleton
@AutoLoad
public class ThemeRepository {

  private final ResourcePackProvider resourcePackProvider;
  private final ResourceLocationProvider resourceLocationProvider;
  private final Theme.Factory themeFactory;
  private final AtomicInteger themeScriptCount;
  private Theme activeTheme;
  private boolean active = true;

  @Inject
  private ThemeRepository(
      ResourcePackProvider resourcePackProvider,
      ResourceLocationProvider resourceLocationProvider,
      Theme.Factory themeFactory,
      LabyfySecurityManager securityManager,
      ThemePermissionChecker permissionChecker) {
    this.resourcePackProvider = resourcePackProvider;
    this.resourceLocationProvider = resourceLocationProvider;
    this.themeFactory = themeFactory;
    this.themeScriptCount = new AtomicInteger(0);
    securityManager.installChecker(permissionChecker);
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

    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    assert contextClassLoader instanceof RootClassLoader;
    RootClassLoader rootClassLoader = (RootClassLoader) contextClassLoader;

    Collections.list(rootClassLoader.commonFindAllResources()).stream()
        .filter(url -> !url.getPath().endsWith(".class"))
        .filter(url -> StringUtils.countMatches(url.getPath(), "assets/minecraft/labyfy/themes") == 1)
        .map(url -> url.getPath().substring(url.getPath().indexOf("assets/minecraft/labyfy/themes") + "assets/minecraft/".length()))
        .forEach(
            path -> {
              try {
                bytes.put(
                    path.replaceFirst("labyfy/themes/", ""),
                    IOUtils.toByteArray(
                        this.resourceLocationProvider.get(path).openInputStream()));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });

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

    String className = "ThemeGroovyScript" + themeScriptCount.incrementAndGet() + ".groovy";
    ThemeGroovyCodeSourceShim codeSource = new ThemeGroovyCodeSourceShim(source, className);
    T handle = (T) new GroovyShell(compilerConfiguration).parse(codeSource);

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
