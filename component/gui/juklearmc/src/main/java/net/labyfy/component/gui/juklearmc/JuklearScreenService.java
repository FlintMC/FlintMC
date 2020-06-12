package net.labyfy.component.gui.juklearmc;

import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen;
import net.labyfy.component.gui.name.ScreenName;
import net.labyfy.component.inject.InjectionHolder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Service(JuklearScreen.class)
public class JuklearScreenService implements ServiceHandler {
  private final JuklearMC juklearMC;

  @Inject
  private JuklearScreenService(JuklearMC juklearMC) {
    this.juklearMC = juklearMC;
  }

  @Override
  public void discover(Identifier.Base property) {
    LocatedIdentifiedAnnotation annotation = property.getProperty().getLocatedIdentifiedAnnotation();

    Class<? extends JuklearMCScreen> clazz = annotation.getLocation();
    JuklearScreen data = annotation.getAnnotation();
    ScreenName overwrittenName = ScreenName.typed(data.type(), data.value());

    JuklearMCScreen instance = InjectionHolder.getInjectedInstance(clazz);
    juklearMC.overwriteScreen(overwrittenName, instance);
  }
}
