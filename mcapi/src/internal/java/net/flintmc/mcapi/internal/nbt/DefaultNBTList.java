package net.flintmc.mcapi.internal.nbt;

import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.NBTList;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/** Default implementation the {@link NBTList}. */
@Implement(NBTList.class)
public class DefaultNBTList implements NBTList {

  private final List<NBT> tags;
  private int subtagIdentifier;

  @AssistedInject
  private DefaultNBTList(@Assisted("subtagIdentifier") int subtagIdentifier) {
    this.tags = Lists.newArrayList();
    this.subtagIdentifier = subtagIdentifier;
  }

  /** {@inheritDoc} */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_LIST;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.clear();
    int length = inputStream.getDataInputStream().readInt();
    for (int i = 0; i < length; i++) {
      NBT tag = inputStream.readTag(this.subtagIdentifier);
      System.out.println(tag.asString());
      this.tags.add(tag);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    if (this.tags.isEmpty()) {
      this.subtagIdentifier = 0;
    } else {
      this.subtagIdentifier = this.tags.get(0).getIdentifier().getIdentifier();
    }

    outputStream.getDataOutputStream().writeByte(this.subtagIdentifier);
    outputStream.getDataOutputStream().writeInt(this.tags.size());

    for (NBT tag : this.tags) {
      tag.writeContents(outputStream);
    }
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return this.tags.stream().map(NBT::asString).collect(Collectors.joining(","));
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<NBT> iterator() {
    return this.tags.iterator();
  }

  /** {@inheritDoc} */
  @Override
  public void add(NBT tag) {
    this.tags.add(tag);
  }

  /** {@inheritDoc} */
  @Override
  public void set(int index, NBT tag) {
    this.tags.set(index, tag);
  }

  /** {@inheritDoc} */
  @Override
  public NBT get(int index) {
    return this.tags.get(index);
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    this.tags.clear();
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return this.tags.size();
  }
}
