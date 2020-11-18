package net.flintmc.util.mojang.internal.profile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

@Singleton
@CacheIO
public class GameProfileIO implements CachedObjectIO<GameProfile> {

  private final GameProfile.Factory profileFactory;
  private final Property.Factory propertyFactory;

  @Inject
  private GameProfileIO(GameProfile.Factory profileFactory, Property.Factory propertyFactory) {
    this.profileFactory = profileFactory;
    this.propertyFactory = propertyFactory;
  }

  @Override
  public Class<GameProfile> getType() {
    return GameProfile.class;
  }

  @Override
  public void write(GameProfile profile, DataOutput output) throws IOException {
    output.writeLong(profile.getUniqueId().getMostSignificantBits());
    output.writeLong(profile.getUniqueId().getLeastSignificantBits());

    output.writeUTF(profile.getName());

    PropertyMap properties = profile.getProperties();
    output.writeByte(properties.size());

    for (Property property : properties.values()) {
      output.writeUTF(property.getName());
      output.writeUTF(property.getValue());
      output.writeBoolean(property.hasSignature());
      if (property.hasSignature()) {
        output.writeUTF(property.getSignature());
      }
    }
  }

  @Override
  public GameProfile read(DataInput input) throws IOException {
    GameProfile profile =
        this.profileFactory.create(new UUID(input.readLong(), input.readLong()), input.readUTF());

    int size = input.readByte();
    for (int i = 0; i < size; i++) {
      String name = input.readUTF();
      String value = input.readUTF();
      String signature = input.readBoolean() ? input.readUTF() : null;
      if (signature != null) {
        profile.getProperties().put(name, this.propertyFactory.create(name, value, signature));
      } else {
        profile.getProperties().put(name, this.propertyFactory.create(name, value));
      }
    }

    return profile;
  }
}
