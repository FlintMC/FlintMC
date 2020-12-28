/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.gui.webgui;

/** Represents a single view (imagine like a browser tab) which can display content. */
public interface WebGuiView {
  /**
   * Closes the view and disposes its native resources. Note that closing the view can have further
   * impact such as closing the window if the view has wrapped a window.
   */
  void close();

  /**
   * Enables or disables transparency for the view. The effects of this depend on the view and what
   * kind of object it is rendered to. This is mainly intended to boost performance of the main
   * minecraft window, since when the main view is not transparent, rendering the world can be
   * skipped. Views other than the main view may or may not respect it.
   *
   * @param transparent {@code true} if the view should become transparent, {@code false} to make
   *     the view opaque
   */
  void setTransparent(boolean transparent);

  /**
   * Retrieves the current URL of the view.
   *
   * @return The current view URL
   */
  String getURL();

  /**
   * Navigates the view to the given URL.
   *
   * @param url The url to navigate to
   */
  void setURL(String url);

  /**
   * Changes the scale of the view.
   *
   * @param scale The new scale
   */
  void setScale(float scale);

  /**
   * Transports a Java object to Javascript or deletes a global Javascript object.
   *
   * @param key The name of the object
   * @param value The Java instance of the object to set, or {@code null}, to remove it
   */
  default void setGlobalJavascriptObject(String key, Object value) {
    setGlobalJavascriptObject(key, value, value == null ? Object.class : value.getClass());
  }

  /**
   * Transports a Java object to Javascript or deletes a global Javascript object.
   *
   * @param <T> The type to bridge
   * @param key The name of the object
   * @param value The Java instance of the object to set, or {@code null}, to remove it
   * @param clazz The class to use as the bridging base
   */
  <T> void setGlobalJavascriptObject(String key, T value, Class<? extends T> clazz);
}
