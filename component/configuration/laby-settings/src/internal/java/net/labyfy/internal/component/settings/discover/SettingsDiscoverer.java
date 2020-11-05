package net.labyfy.internal.component.settings.discover;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.event.subscribe.Subscribe.Phase;
import net.labyfy.component.settings.InvalidSettingsException;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.annotation.ui.Category;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

@Singleton
public class SettingsDiscoverer {

  private final SettingsProvider settingsProvider;
  private final RegisteredCategory.Factory categoryFactory;
  private final RegisteredSetting.Factory settingFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public SettingsDiscoverer(SettingsProvider settingsProvider, RegisteredCategory.Factory categoryFactory,
                            RegisteredSetting.Factory settingFactory, ComponentAnnotationSerializer annotationSerializer) {
    this.settingsProvider = settingsProvider;
    this.categoryFactory = categoryFactory;
    this.settingFactory = settingFactory;
    this.annotationSerializer = annotationSerializer;
  }

  @Subscribe(phase = Phase.POST)
  public void handleConfigDiscovered(ConfigDiscoveredEvent event) {
    ParsedConfig config = event.getConfig();

    for (ConfigObjectReference reference : config.getConfigReferences()) {
      this.handleSetting(config, reference);
    }
  }

  private void handleSetting(ParsedConfig config, ConfigObjectReference reference) {
    Annotation annotation = this.findSettingAnnotation(reference);
    if (annotation == null) {
      return;
    }
    ApplicableSetting applicableSetting = annotation.annotationType().getAnnotation(ApplicableSetting.class);

    Type type = reference.getSerializedType();
    if (!(type instanceof Class)) {
      this.throwInvalidSetting(reference, annotation, config, type);
    }
    Class<?> clazz = (Class<?>) type;
    if (clazz.isPrimitive()) {
      clazz = PrimitiveTypeLoader.getWrappedClass(clazz);
    }

    Class<?> finalClazz = clazz;
    boolean assignable = Arrays.stream(applicableSetting.value()).anyMatch(required -> {
      if (required.isPrimitive()) {
        required = PrimitiveTypeLoader.getWrappedClass(required);
      }
      return required.isAssignableFrom(finalClazz);
    });

    if (!assignable) {
      // we need assignableFrom because for example the EnumDropDown uses Enum.class as the required parameter
      // and can't specify more specific values
      throw new InvalidSettingsException("Cannot register setting on '" + reference.getKey() + "' in config '"
          + config.getConfigName() + "' because none of the allowed types for " + annotation.annotationType().getName()
          + " match " + type.getTypeName());
    }

    String category = this.findCategoryName(reference);

    RegisteredSetting registeredSetting = this.settingFactory.create(annotation.annotationType(), config, category, reference);
    this.settingsProvider.registerSetting(registeredSetting);
  }

  private void throwInvalidSetting(ConfigObjectReference reference, Annotation annotation, ParsedConfig config,
                                   Type type) {
    throw new InvalidSettingsException("Cannot register setting on '" + reference.getKey() + "' in config '"
        + config.getConfigName() + "' because none of the allowed types for " + annotation.annotationType().getName()
        + " match " + type.getTypeName());
  }

  private String findCategoryName(ConfigObjectReference reference) {
    Category category = reference.findLastAnnotation(Category.class);
    DefineCategory define = reference.findLastAnnotation(DefineCategory.class);

    String name = null;

    if (define != null) {
      name = define.name();

      // define the category if it doesn't exist yet (it may already exist if the annotation is placed on an interface,
      // all methods inside of it would define a new category)
      if (this.settingsProvider.getCategory(name) == null) {
        this.settingsProvider.registerCategory(this.categoryFactory.create(name,
            this.annotationSerializer.deserialize(define.displayName(), define.name()),
            this.annotationSerializer.deserialize(define.description())
        ));
      }

    }

    if (category != null) {
      name = category.value();
    }

    return name;
  }

  private Annotation findSettingAnnotation(ConfigObjectReference reference) {
    for (Annotation annotation : reference.findAllAnnotations()) {
      if (annotation.annotationType().isAnnotationPresent(ApplicableSetting.class)) {
        return annotation;
      }
    }

    return null;
  }

}
