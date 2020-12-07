package net.flintmc.mcapi.entity;

public class EntityNotLoadedException extends RuntimeException {

  public static final EntityNotLoadedException INSTANCE = new EntityNotLoadedException();

  private EntityNotLoadedException() {
    super("Cannot use the Entity module when not inside of a world");
  }
}
