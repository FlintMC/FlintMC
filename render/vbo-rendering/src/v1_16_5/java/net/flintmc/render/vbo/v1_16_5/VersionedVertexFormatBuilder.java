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

package net.flintmc.render.vbo.v1_16_5;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexFormat;
import net.flintmc.render.vbo.VertexFormatBuilder;

/**
 * {@inheritDoc}
 */
@Singleton
@Implement(value = VertexFormatBuilder.class, version = "1.16.5")
public class VersionedVertexFormatBuilder implements VertexFormatBuilder {

  private final VertexFormat.Factory vertexFormatFactory;
  private List<VertexAttribute> currentAttributes;

  @Inject
  private VersionedVertexFormatBuilder(VertexFormat.Factory vertexFormatFactory) {
    this.currentAttributes = new ArrayList<>();
    this.vertexFormatFactory = vertexFormatFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexFormatBuilder addAttribute(VertexAttribute attribute) {
    this.currentAttributes.add(attribute);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexFormat build() {
    VertexFormat format = this.vertexFormatFactory.create(this.currentAttributes);
    this.currentAttributes = new ArrayList<>();
    return format;
  }
}
