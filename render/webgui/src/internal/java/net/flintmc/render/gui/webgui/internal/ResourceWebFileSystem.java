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

package net.flintmc.render.gui.webgui.internal;

import com.google.inject.Singleton;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/** Provides a web filesystem for reading class resources. */
@Singleton
@WebFileSystem("class-resource")
public class ResourceWebFileSystem implements WebFileSystemHandler {

  /** {@inheritDoc} */
  @Override
  public boolean existsFile(String path) {
    return getClass().getResource(path) != null;
  }

  /** {@inheritDoc} */
  @Override
  public WebResource getFile(final String path) throws FileNotFoundException {
    return new URLWebResource(path);
  }
}
