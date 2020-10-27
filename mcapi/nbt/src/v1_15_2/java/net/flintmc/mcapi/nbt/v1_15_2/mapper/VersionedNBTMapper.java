package net.flintmc.mcapi.nbt.v1_15_2.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.*;
import net.flintmc.mcapi.nbt.array.NBTByteArray;
import net.flintmc.mcapi.nbt.array.NBTIntArray;
import net.flintmc.mcapi.nbt.array.NBTLongArray;
import net.flintmc.mcapi.nbt.mapper.NBTMapper;
import net.minecraft.nbt.*;

import java.util.Map;

@Singleton
@Implement(value = NBTMapper.class, version = "1.15.2")
public class VersionedNBTMapper implements NBTMapper {

  private final NBTCreator creator;

  @Inject
  private VersionedNBTMapper(NBTCreator creator) {
    this.creator = creator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBT fromMinecraftNBT(Object handle) {
    if (handle == null) return null;
    if (!(handle instanceof INBT)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + INBT.class.getName());
    }

    INBT nbt = (INBT) handle;

    if (nbt instanceof StringNBT) {
      StringNBT stringNBT = (StringNBT) nbt;
      return this.creator.createNbtString(stringNBT.getString());
    } else if (nbt instanceof IntNBT) {
      IntNBT intNBT = (IntNBT) nbt;
      return this.creator.createNbtInt(intNBT.getInt());
    } else if (nbt instanceof ShortNBT) {
      ShortNBT shortNBT = (ShortNBT) nbt;
      return this.creator.createNbtShort(shortNBT.getShort());
    } else if (nbt instanceof DoubleNBT) {
      DoubleNBT doubleNBT = (DoubleNBT) nbt;
      return this.creator.createNbtDouble(doubleNBT.getDouble());
    } else if (nbt instanceof FloatNBT) {
      FloatNBT floatNBT = (FloatNBT) nbt;
      return this.creator.createNbtFloat(floatNBT.getFloat());
    } else if (nbt instanceof LongNBT) {
      LongNBT longNBT = (LongNBT) nbt;
      return this.creator.createNbtLong(longNBT.getLong());
    } else if (nbt instanceof ByteArrayNBT) {
      ByteArrayNBT byteArrayNBT = (ByteArrayNBT) nbt;
      return this.creator.createNbtByteArray(byteArrayNBT.getByteArray());
    } else if (nbt instanceof IntArrayNBT) {
      IntArrayNBT intArrayNBT = (IntArrayNBT) nbt;
      return this.creator.createNbtIntArray(intArrayNBT.getIntArray());
    } else if (nbt instanceof LongArrayNBT) {
      LongArrayNBT longArrayNBT = (LongArrayNBT) nbt;
      return this.creator.createNbtLongArray(longArrayNBT.getAsLongArray());
    } else if (nbt instanceof CompoundNBT) {
      CompoundNBT compoundNBT = (CompoundNBT) nbt;
      NBTCompound compound = this.creator.createNbtCompound();

      for (String key : compoundNBT.keySet()) {
        compound.set(key, this.fromMinecraftNBT(compoundNBT.get(key)));
      }

      return compound;
    } else if (nbt instanceof ListNBT) {
      ListNBT listNBT = (ListNBT) nbt;
      NBTList list = this.creator.createNbtList(listNBT.getTagType());
      for (INBT inbt : listNBT) {
        list.add(this.fromMinecraftNBT(inbt));
      }
      return list;
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftNBT(NBT nbt) {

    if (nbt instanceof NBTByte) {
      NBTByte nbtByte = (NBTByte) nbt;
      return ByteNBT.valueOf(nbtByte.asByte());
    } else if (nbt instanceof NBTDouble) {
      NBTDouble nbtDouble = (NBTDouble) nbt;
      return DoubleNBT.valueOf(nbtDouble.asDouble());
    } else if (nbt instanceof NBTFloat) {
      NBTFloat nbtFloat = (NBTFloat) nbt;
      return FloatNBT.valueOf(nbtFloat.asFloat());
    } else if (nbt instanceof NBTInt) {
      NBTInt nbtInt = (NBTInt) nbt;
      return IntNBT.valueOf(nbtInt.asInt());
    } else if (nbt instanceof NBTShort) {
      NBTShort nbtShort = (NBTShort) nbt;
      return ShortNBT.valueOf(nbtShort.asShort());
    } else if (nbt instanceof NBTLong) {
      NBTLong nbtLong = (NBTLong) nbt;
      return LongNBT.valueOf(nbtLong.asLong());
    } else if (nbt instanceof NBTString) {
      NBTString nbtString = (NBTString) nbt;
      return StringNBT.valueOf(nbtString.asString());
    } else if (nbt instanceof NBTList) {
      NBTList list = (NBTList) nbt;
      ListNBT listNBT = new ListNBT();

      for (int i = 0; i < list.size(); i++) {
        listNBT.add(i, (INBT) this.toMinecraftNBT(list.get(i)));
      }

      return listNBT;
    } else if (nbt instanceof NBTCompound) {
      NBTCompound nbtCompound = (NBTCompound) nbt;
      CompoundNBT compoundNBT = new CompoundNBT();

      for (Map.Entry<String, NBT> entry : nbtCompound.getTags().entrySet()) {
        compoundNBT.put(entry.getKey(), (INBT) this.toMinecraftNBT(entry.getValue()));
      }

      return compoundNBT;
    } else if (nbt instanceof NBTByteArray) {
      NBTByteArray nbtByteArray = (NBTByteArray) nbt;
      return new ByteArrayNBT(nbtByteArray.asArray());
    } else if (nbt instanceof NBTLongArray) {
      NBTLongArray nbtLongArray = (NBTLongArray) nbt;
      return new LongArrayNBT(nbtLongArray.asArray());
    } else if (nbt instanceof NBTIntArray) {
      NBTIntArray nbtIntArray = (NBTIntArray) nbt;
      return new IntArrayNBT(nbtIntArray.asArray());
    }

    return null;
  }
}
