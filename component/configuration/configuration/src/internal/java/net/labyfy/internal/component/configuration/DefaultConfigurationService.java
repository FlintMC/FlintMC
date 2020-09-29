package net.labyfy.internal.component.configuration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.configuration.Configuration;
import net.labyfy.component.configuration.ConfigurationService;
import net.labyfy.component.configuration.MethodSetting;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
@Service(value = Configuration.class, priority = -10000)
@Implement(ConfigurationService.class)
public class DefaultConfigurationService implements ServiceHandler, ConfigurationService {

  private final AtomicReference<Injector> injectorReference;

  private final MethodSetting.Factory methodSettingFactory;

  private final MethodHandles.Lookup lookup;
  private final Multimap<Class<?>, MethodSetting> filteredValueMethods;
  private final Multimap<Class<?>, MethodSetting> filteredUpdateMethods;
  private final Multimap<Class<?>, Field> filteredFields;

  private File file;

  @Inject
  public DefaultConfigurationService(
          @Named("injectorReference") AtomicReference injectorReference,
          MethodSetting.Factory methodSettingFactory) {
    this.injectorReference = injectorReference;
    this.methodSettingFactory = methodSettingFactory;
    this.filteredUpdateMethods = HashMultimap.create();
    this.filteredValueMethods = HashMultimap.create();
    this.filteredFields = HashMultimap.create();
    this.lookup = MethodHandles.lookup();
  }

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {

    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    Configuration configuration = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();

    String path = configuration.value();

    File directory = null;
    File configFile = null;

    if (path.contains("/")) {
      String[] split = path.split("/");

      StringBuilder directories = new StringBuilder();
      String configurationFile = "";

      for (int i = 0; i < split.length; i++) {
        if (i == split.length - 1) {
          configurationFile = split[i];
          break;
        }

        directories.append(split[i]).append("/");
      }

      directory = new File(directories.toString());
      configFile = new File(directory, configurationFile + ".json");
    }

    if (!directory.exists()) {
      directory.mkdirs();
    }

    this.file = configFile;

    Object instance = this.injectorReference.get().getInstance(location);

    for (Field declaredField : location.getDeclaredFields()) {
      if (declaredField.isAnnotationPresent(Configuration.Entry.class)) {
        this.filteredFields.put(location, declaredField);
      }
    }


    for (Method declaredMethod : location.getDeclaredMethods()) {
      if (declaredMethod.isAnnotationPresent(Configuration.Entry.class)) {
        Configuration.Entry setting = declaredMethod.getAnnotation(Configuration.Entry.class);
        MethodSetting methodSetting = this.methodSettingFactory.create(instance, declaredMethod, setting.value());

        if (declaredMethod.getReturnType().equals(Void.TYPE)) {
          this.filteredUpdateMethods.put(location, methodSetting);
        } else {
          this.filteredValueMethods.put(location, methodSetting);
        }

      }
    }

  }

  @Override
  public void load(Class<?> configurationClass) {
    if (!this.file.exists()) {
      this.save(configurationClass);
      return;
    }

    for (Field field : this.filteredFields.get(configurationClass)) {

    }

    for (MethodSetting method : this.filteredValueMethods.get(configurationClass)) {

    }
  }

  @Override
  public void save(Class<?> configurationClass) {
    JsonObject configuration = new JsonObject();

    for (Field field : this.filteredFields.get(configurationClass)) {

    }

    for (MethodSetting method : this.filteredUpdateMethods.get(configurationClass)) {

    }

    try {
      this.writeConfiguration(configuration);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void writeConfiguration(JsonObject object) throws FileNotFoundException {
    PrintWriter printWriter = new PrintWriter(new FileOutputStream(this.file));
    printWriter.write(object.toString());
    printWriter.flush();
    printWriter.close();
  }

}
