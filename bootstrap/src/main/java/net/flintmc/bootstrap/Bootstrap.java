package net.flintmc.bootstrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.flintmc.installer.impl.repository.models.DependencyDescriptionModel;
import net.flintmc.installer.impl.repository.models.PackageModel;
import net.flintmc.installer.impl.repository.models.install.InstallInstructionDeserializer;
import net.flintmc.installer.impl.repository.models.install.InstallInstructionModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Bootstrap {

  public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    new Bootstrap().boot(args);
  }

  private final Map<String, PackageModel> packageModels = new HashMap<>();

  private final Gson gson =
      new GsonBuilder()
          .setDateFormat(DateFormat.FULL, DateFormat.FULL)
          .setPrettyPrinting()
          .disableHtmlEscaping()
          .registerTypeAdapter(InstallInstructionModel.class, new InstallInstructionDeserializer())
          .create();

  private void boot(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    PackageModel bootstrapModel = getPackageModel(getClass().getClassLoader().getResourceAsStream("manifest.json"));

    this.loadDependencies(bootstrapModel);
    Collection<String> runtimeClasspath = new HashSet<>();
    this.packageModels.forEach((path, model) -> {
      runtimeClasspath.addAll(model.getRuntimeClasspath());
    });
    URL[] urls = runtimeClasspath.stream()
        .map(url -> url.replace("${FLINT_LIBRARY_DIR}", "libraries/").replace("${FLINT_PACKAGE_DIR}", "flint/packages/"))
        .map(url -> {
          try {
            return Paths.get(url).toAbsolutePath().toUri().toURL();
          } catch (MalformedURLException e) {
            e.printStackTrace();
          }
          return null;
        })
        .filter(Objects::nonNull)
        .toArray(URL[]::new);

    URLClassLoader urlClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    Class<?> flintLauncherClass = urlClassLoader.loadClass("net.flintmc.launcher.FlintLauncher");
    flintLauncherClass.getDeclaredMethod("main", String[].class, URL[].class).invoke(null, args, urls);
  }

  private PackageModel getPackageModel(InputStream inputStream) throws IOException {
    byte[] data = new byte[inputStream.available()];
    inputStream.read(data);
    inputStream.close();
    return gson.fromJson(new String(data, StandardCharsets.UTF_8), PackageModel.class);
  }

  private void loadDependencies(PackageModel packageModel) throws IOException {
    for (String runtimeClasspathCandidates : packageModel.getRuntimeClasspath()) {
      if (!runtimeClasspathCandidates.toLowerCase().endsWith(".jar")) continue;
      String dependencyPath = runtimeClasspathCandidates.replace("${FLINT_LIBRARY_DIR}", "libraries/").replace("${FLINT_PACKAGE_DIR}", "flint/packages/");

      JarFile jarFile = new JarFile(dependencyPath);
      JarEntry manifestEntry = findManifestEntry(jarFile);

      if (manifestEntry != null) {
        PackageModel dependencyModel = getPackageModel(jarFile.getInputStream(manifestEntry));

        if (packageModels.containsKey(dependencyPath))
          continue;
        packageModels.put(dependencyPath, dependencyModel);
        this.loadDependencies(dependencyModel);
      }

      jarFile.close();
    }
  }

  private JarEntry findManifestEntry(JarFile jarFile) {
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements()) {
      JarEntry jarEntry = entries.nextElement();
      if (jarEntry.getName().equals("manifest.json"))
        return jarEntry;
    }
    return null;
  }

}
