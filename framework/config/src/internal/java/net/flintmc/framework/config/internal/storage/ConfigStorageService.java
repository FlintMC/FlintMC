package net.flintmc.framework.config.internal.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.config.storage.StoragePriority;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.Identifier;

@Singleton
@Service(
    value = StoragePriority.class,
    priority =
        1 /* higher than from the ConfigGenerator so that the configs are available for reading in this service */)
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
