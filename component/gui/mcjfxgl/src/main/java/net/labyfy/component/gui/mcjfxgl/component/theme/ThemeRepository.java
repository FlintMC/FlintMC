package net.labyfy.component.gui.mcjfxgl.component.theme;

import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControlBase;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;
import net.labyfy.component.inject.event.Event;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePack;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Singleton
public class ThemeRepository {

  private final ResourcePackProvider resourcePackProvider;
  private final ResourceLocationProvider resourceLocationProvider;
  private final Collection<Theme> themes;
  private Theme activeTheme;

  @Inject
  private ThemeRepository(
      ResourcePackProvider resourcePackProvider,
      ResourceLocationProvider resourceLocationProvider) {
    this.resourcePackProvider = resourcePackProvider;
    this.resourceLocationProvider = resourceLocationProvider;
    this.themes = new HashSet<>();
  }

  @Event(ResourcePackReloadEvent.class)
  public void reloadThemes() throws IOException {
    themes.clear();
    Map<String, byte[]> bytes = new HashMap<>();
    for (ResourceLocation labyfy : this.resourceLocationProvider.getLoaded("labyfy")) {
      bytes.put(
          labyfy.getPath().replaceFirst("labyfy/themes/", ""),
          IOUtils.toByteArray(labyfy.openInputStream()));
    }

    if (!bytes.containsKey("theme.groovy")) return;

    ThemeConfig themeConfig =
        this.parseAndRun(bytes.get("theme.groovy"), ThemeConfig.Handle.class).toModel();

    Multimap<Class<? extends McJfxGLControlBase<?>>, ThemeComponentStyle> stylesMultimap =
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

    for (Class<? extends McJfxGLControlBase<?>> key : stylesMultimap.keys()) {
      Collection<ThemeComponentStyle> themeComponentStyles = stylesMultimap.get(key);
      if (themeComponentStyles.size() == 1) continue;
      stylesMultimap.removeAll(key);
      System.out.println("Remove invalid component style for " + key.getName());
    }

    this.themes.add(
        new Theme(
            themeConfig,
            stylesMultimap.entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
    this.setActive(this.get("default"));
  }

  public Theme get(String name) {
    for (Theme theme : this.themes) {
      if (theme.getConfig().getInfo().getName().equals(name)) {
        return theme;
      }
    }
    return null;
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

  public ThemeRepository setActive(Theme activeTheme) {
    this.activeTheme = activeTheme;
    return this;
  }

  public Theme getActive() {
    return activeTheme;
  }
}
