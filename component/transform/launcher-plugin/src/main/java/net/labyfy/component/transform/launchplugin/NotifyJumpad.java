package net.labyfy.component.transform.launchplugin;

import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

public class NotifyJumpad {
  public static void notifyService(Class<?> clazz) throws ServiceNotFoundException {
    EntryPoint.notifyService(clazz);
  }
}
