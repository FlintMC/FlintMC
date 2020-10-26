package net.labyfy.component.items;

import com.google.gson.JsonObject;

/**
 * Serializer for the (de-)serialization of an {@link ItemStack} into a json that can be read be vanilla minecraft.
 */
public interface ItemStackSerializer {

  /**
   * Parses an {@link ItemStack} out of the given json object.
   *
   * @param object The non-null object to parse the item out
   * @return The parsed item or {@code null} if an invalid json has been provided
   * @throws NullPointerException If no item with the id in the given json exists
   */
  ItemStack fromJson(JsonObject object);

  /**
   * Serializes the given {@link ItemStack} into a new json object to be read by vanilla minecraft.
   *
   * @param itemStack The non-null item to create the json from
   * @return The new non-null json object out of the given item
   */
  JsonObject toJson(ItemStack itemStack);

}
