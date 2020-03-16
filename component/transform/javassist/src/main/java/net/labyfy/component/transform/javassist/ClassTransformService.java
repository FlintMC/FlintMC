package net.labyfy.component.transform.javassist;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

import javax.inject.Singleton;

@Singleton
@MinecraftTransformer
@Service(ClassTransform.class)
public class ClassTransformService implements ServiceHandler, IClassTransformer {

  public void discover(Identifier.Base property) {
    System.out.println(
        "Discovered class transformer " + property.getProperty().getLocatedIdentifiedAnnotation().getLocation());
  }

  public byte[] transform(String s, String s1, byte[] bytes) {
    return bytes;
  }
}
