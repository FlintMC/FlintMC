package net.labyfy.component.stereotype;

import java.util.Objects;

public class NameSpacedKey {

  /**
   * The default splitter between the name space and the key.
   */
  private static final char DEFAULT_SPLITTER = ':';

  private final String nameSpace;
  private final String key;

  private NameSpacedKey(String nameSpace, String key) {
    this.nameSpace = nameSpace;
    this.key = key;
  }

  public static NameSpacedKey of(String nameSpace, String key) {
    return new NameSpacedKey(nameSpace, key);
  }

  public static NameSpacedKey minecraft(String key) {
    return of("minecraft", key);
  }

  public static NameSpacedKey parse(String in) {
    return parse(in, DEFAULT_SPLITTER);
  }

  public static NameSpacedKey parse(String in, char splitter) {
    int index = in.indexOf(splitter);
    if (index == -1 || index > in.length() - 1) {
      return minecraft(in);
    }

    return of(in.substring(0, index), in.substring(index + 1));
  }

  public String getNameSpace() {
    return this.nameSpace;
  }

  public String getKey() {
    return this.key;
  }

  @Override
  public String toString() {
    return this.nameSpace + DEFAULT_SPLITTER + this.key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NameSpacedKey that = (NameSpacedKey) o;
    return nameSpace.equals(that.nameSpace) && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameSpace, key);
  }
}
