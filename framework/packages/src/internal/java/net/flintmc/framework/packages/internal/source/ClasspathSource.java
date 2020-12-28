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

package net.flintmc.framework.packages.internal.source;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/** Represents the classpath as a source. */
public class ClasspathSource implements PackageSource {
  /** {@inheritDoc} */
  @Override
  public URL findResource(String path) {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }

  /** {@inheritDoc} */
  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }

  /** {@inheritDoc} */
  @Override
  public Enumeration<URL> findAllResources() throws IOException {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet, tracked by issue #43");
  }
}
