package net.labyfy.component.config;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.IncludeStorage;

import java.util.Map;

// TODO remove?

@Config
// Dieses Interface soll in der Datei automatisch beim Start geladen und bei Änderungen wieder gespeichert werden

// kein @Storage -> überall speichern
//@Storage({"local", "asdf"}) // nur lokal und in "asdf" gespeichert
//@Storage("online") // nur online gespeichert
//@ExcludeStorage("local") // überall außer lokal gespeichert - nützlich für sowas wie die MinecraftConfiguration, die ja eh schon lokal gespeichert wird von MC selbst
public interface ExampleConfig {

  // kein @Storage -> @Storage des Interfaces
  //@Storage("online") // nur online gespeichert
  @IncludeStorage("1234")
  String getX();

  @IncludeStorage("5678")
  void setX(String x);

  @IncludeStorage("local")
    // nur lokal gespeichert
  double getZ();

  //@Storage("local") // nur lokal gespeichert
  void setZ(double z);

  // eine Instanz von dem Local interface wird automatisch erstellt, setter methoden nicht notwendig, aber möglich
  Local getY();

  @ExcludeStorage("local")
  Local getXY();

  Boolean getE(SomeEnum x);

  Map<SomeEnum, Boolean> getAllE();

  void setAllE(Map<SomeEnum, Boolean> map);

  void setE(SomeEnum x, Boolean y);

  enum SomeEnum {

    E1,
    E2,
    E3,
    E4,

  }

  @IncludeStorage("local") // nur lokal gespeichert - könnte auch auf getXY und getY stehen
  interface Local {

    int getLocalInt();

    void setLocalInt(int a);

  }

}
