package net.labyfy.component.transform.javassist;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;

@Service(ClassTransform.class)
public class ClassTransformService implements ServiceHandler {

  public void discover(Identifier.Base property) {}
}
