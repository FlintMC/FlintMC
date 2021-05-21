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

package net.flintmc.render.gui.windowing;

import net.flintmc.render.gui.screen.ScreenName;

import java.util.Collection;

/**
 * Renderer for windows.
 */
public interface WindowRenderer {

    /**
     * Called when the renderer is added to a window. The OpenGL context in this method is the one
     * used for the window.
     */
    void initialize();

    /**
     * Determines if this renderer is taking full control of the content of the window.
     * If this setting is {@code true} the rendering of every minecraft window in {@link WindowRenderer#getIntrusiveScreens()}
     * will be cancelled when this renderer is registered.
     *
     * @return {@code true} to mark the renderer as intrusive, {@code false} to mark it as cooperative
     */
    boolean isIntrusive();

    /**
     * Used in {@link WindowRenderer#isIntrusive()} to check if the currently rendered screen should be rendered intrusively.
     * Every entry in this collection determines if this renderer is taking full control of the content of the window.
     * If it is in it the default render logic of minecraft will be disabled.
     *
     * @return all minecraft screens that should be rendered intrusively
     */
    Collection<ScreenName> getIntrusiveScreens();

    /**
     * Called when window needs to be rendered. The OpenGL context in this method is the one used for
     * the window.
     */
    void render();

    /**
     * Called when the renderer is removed from a window. The OpenGL context in this method is the one
     * used for the window.
     */
    void cleanup();
}
