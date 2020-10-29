package net.labyfy.internal.component.settings.discover;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.event.subscribe.Subscribe.Phase;
import net.labyfy.component.settings.InvalidSettingsException;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.annotation.Component;
import net.labyfy.component.settings.annotation.ui.Category;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

@Singleton
public class SettingsDiscoverer {

  private final SettingsProvider settingsProvider;
  private final RegisteredCategory.Factory categoryFactory;
  private final RegisteredSetting.Factory settingFactory;
  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentBuilder.Factory builderFactory;

  @Inject
  public SettingsDiscoverer(SettingsProvider settingsProvider, RegisteredCategory.Factory categoryFactory,
                            RegisteredSetting.Factory settingFactory, ComponentSerializer.Factory serializerFactory,
                            ComponentBuilder.Factory builderFactory) {
    this.settingsProvider = settingsProvider;
    this.categoryFactory = categoryFactory;
    this.settingFactory = settingFactory;
    this.serializerFactory = serializerFactory;
    this.builderFactory = builderFactory;
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
    if (!(type instanceof Class)
        || Arrays.stream(applicableSetting.value()).noneMatch(required -> required.isAssignableFrom((Class<?>) type))) {
      // we need assignableFrom because for example the EnumDropDown uses Enum.class as the required parameter
      // and can't specify more specific values
      throw new InvalidSettingsException("Cannot register setting on " + reference.getKey() + " in config "
          + config.getConfigName() + " because none of the allowed types for " + annotation.annotationType().getName()
          + " match " + type.getTypeName());
    }

    DisplayName named = reference.findLastAnnotation(DisplayName.class);
    Description described = reference.findLastAnnotation(Description.class);

    // a displayName is necessary, if no displayName has been provided, we use the last value from the path keys
    // which is part of the name of the getter/setter
    ChatComponent displayName = this.asComponent(named != null ? named.value() : null, reference.getPathKeys()[reference.getPathKeys().length - 1]);
    // the description is optional and may be displayed when hovering over the setting
    ChatComponent description = this.asComponent(described != null ? described.value() : null, null);

    String category = this.findCategoryName(reference);

    RegisteredSetting registeredSetting =
        this.settingFactory.create(displayName, description, annotation, config, category, reference);
    this.settingsProvider.registerSetting(registeredSetting);
  }

  private String findCategoryName(ConfigObjectReference reference) {
    Category category = reference.findLastAnnotation(Category.class);
    DefineCategory define = reference.findLastAnnotation(DefineCategory.class);

    String name = null;

    if (define != null) {
      name = define.name();

      // define the category if it doesn't exist yet (e.g. if the annotation is placed on an interface, all
      // methods inside of it would define a new category)
      if (this.settingsProvider.getCategory(name) == null) {
        this.settingsProvider.registerCategory(this.categoryFactory.create(name,
            this.asComponent(define.displayName(), null), this.asComponent(define.description(), null)
        ));
      }

    }

    if (category != null) {
      name = category.value();
    }

    return name;
  }

  private ChatComponent asComponent(Component component, String optional) {
    if (component == null) {
      // the optional (if present) will be parsed as a legacy text like "§ctest"
      return optional != null ? this.serializerFactory.legacy().deserialize(optional) : null;
    }
    if (component.translate()) {
      // the value will be used as a translation key
      return this.builderFactory.translation().translationKey(component.value()).build();
    }
    // the value will be parsed as a legacy text like "§ctest"
    return this.serializerFactory.legacy().deserialize(component.value());
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
