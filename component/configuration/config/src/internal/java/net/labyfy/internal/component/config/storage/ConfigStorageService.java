package net.labyfy.internal.component.config.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.config.storage.StoragePriority;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

@Singleton
@Service(value = StoragePriority.class, priority = 1 /* higher than from the ConfigGenerator so that the configs are available for reading in this service */)
public class ConfigStorageService implements ServiceHandler<StoragePriority> {

  private final ConfigStorageProvider storageProvider;

  @Inject
  public ConfigStorageService(ConfigStorageProvider storageProvider) {
    this.storageProvider = storageProvider;
  }


  @Override
  public void discover(AnnotationMeta<StoragePriority> meta) {
    Identifier<CtClass> identifier = meta.getIdentifier();

    this.storageProvider.registerStorage(identifier.getLocation());
  }
}
