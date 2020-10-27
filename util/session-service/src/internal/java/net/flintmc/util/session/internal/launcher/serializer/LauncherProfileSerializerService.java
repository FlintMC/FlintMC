package net.flintmc.util.session.internal.launcher.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.serializer.ProfileSerializerVersion;

@Singleton
@Service(ProfileSerializerVersion.class)
public class LauncherProfileSerializerService implements ServiceHandler<ProfileSerializerVersion> {

  private final LauncherProfileResolver resolver;

  @Inject
  public LauncherProfileSerializerService(LauncherProfileResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void discover(AnnotationMeta<ProfileSerializerVersion> annotationMeta) {
    ProfileSerializerVersion version = annotationMeta.getAnnotation();
    this.resolver.registerSerializer(
        version.value(), annotationMeta.<ClassIdentifier>getIdentifier().getLocation());
  }
}
