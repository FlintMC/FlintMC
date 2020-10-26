package net.labyfy.internal.component.session.launcher.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.labyfy.component.session.launcher.LauncherProfileResolver;
import net.labyfy.component.session.launcher.serializer.ProfileSerializerVersion;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

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
    this.resolver.registerSerializer(version.value(), annotationMeta.<ClassIdentifier>getIdentifier().getLocation());
  }
}
