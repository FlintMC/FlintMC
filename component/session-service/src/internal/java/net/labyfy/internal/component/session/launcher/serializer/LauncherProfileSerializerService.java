package net.labyfy.internal.component.session.launcher.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.session.launcher.LauncherProfileResolver;
import net.labyfy.component.session.launcher.serializer.LauncherProfileSerializer;
import net.labyfy.component.session.launcher.serializer.ProfileSerializerVersion;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

@Singleton
@Service(ProfileSerializerVersion.class)
public class LauncherProfileSerializerService implements ServiceHandler {

  private final LauncherProfileResolver resolver;

  @Inject
  public LauncherProfileSerializerService(LauncherProfileResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void discover(Identifier.Base property) {
    LocatedIdentifiedAnnotation annotation = property.getProperty().getLocatedIdentifiedAnnotation();
    ProfileSerializerVersion version = annotation.getAnnotation();
    Class<? extends LauncherProfileSerializer> serializer = annotation.getLocation();

    this.resolver.registerSerializer(version.value(), InjectionHolder.getInjectedInstance(serializer));
  }
}
