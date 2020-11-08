package net.flintmc.framework.stereotype;

import java.util.Objects;

/** A key with a name space like minecraft:stone. */
public class NameSpacedKey {

  /** The default splitter between the name space and the key. */
  private static final char DEFAULT_SPLITTER = ':';

  private final String nameSpace;
  private final String key;

  private NameSpacedKey(String nameSpace, String key) {
    this.nameSpace = nameSpace;
    this.key = key;
  }

  /**
   * Creates a new namespaced key out of the given namespace and key.
   *
   * @param nameSpace The non-null namespace for the result
   * @param key The non-null key for the result
   * @return The new non-null namespaced key
   */
  public static NameSpacedKey of(String nameSpace, String key) {
    return new NameSpacedKey(nameSpace, key);
  }

  /**
   * Creates a new namespaced key with the namespace {@code minecraft} and the given key.
   *
   * <p>Similar to {@code #of("minecraft", key)}.
   *
   * @param key The non-null key for the result
   * @return The new non-null namespaced key
   */
  public static NameSpacedKey minecraft(String key) {
    return of("minecraft", key);
  }

  /**
   * Parses the given text as a namespaced key at the default splitter ({@code :}). If the input
   * doesn't contain a ':', {@code minecraft} will be used as the namespace and the raw input as the
   * key.
   *
   * @param in The key with the namespace to be parsed (e.g. minecraft:stone)
   * @return The new non-null namespaced key
   */
  public static NameSpacedKey parse(String in) {
    return parse(in, DEFAULT_SPLITTER);
  }

  /**
   * Parses the given text as a namespaced key at the given splitter. If the input doesn't contain
   * the given splitter, {@code minecraft} will be used as the namespace and the raw input as the
   * key.
   *
   * @param in The key with the namespace to be parsed (e.g. minecraft:stone)
   * @param splitter The splitter for splitting the namespace and the key in the input (e.g. :)
   * @return The new non-null namespaced key
   */
  public static NameSpacedKey parse(String in, char splitter) {
    int index = in.indexOf(splitter);
    if (index == -1 || index > in.length() - 1) {
      return minecraft(in);
    }

    return of(in.substring(0, index), in.substring(index + 1));
  }

  /**
   * Retrieves the namespace of this namespaced key (e.g. minecraft).
   *
   * @return The non-null namespace of this namespaced key
   */
  public String getNameSpace() {
    return this.nameSpace;
  }

  /**
   * Retrieves the key of this namespaced key (e.g. stone).
   *
   * @return The non-null key of this namespaced key
   */
  public String getKey() {
    return this.key;
  }

  /**
   * Retrieves this namespaced key in a format that can be parsed by {@link #parse(String)}.
   *
   * @return The non-null namespaced key as a string
   */
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
