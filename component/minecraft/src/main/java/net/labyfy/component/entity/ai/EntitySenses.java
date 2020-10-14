package net.labyfy.component.entity.ai;

import net.labyfy.component.entity.Entity;

public interface EntitySenses {

  void tick();

  boolean canSee(Entity entity);

}
