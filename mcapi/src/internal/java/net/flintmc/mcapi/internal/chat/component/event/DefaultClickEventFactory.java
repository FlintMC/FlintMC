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

package net.flintmc.mcapi.internal.chat.component.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.internal.chat.component.event.DefaultClickEvent.InternalFactory;

@Singleton
@Implement(ClickEvent.Factory.class)
public class DefaultClickEventFactory implements ClickEvent.Factory {

  private final DefaultClickEvent.InternalFactory factory;

  @Inject
  private DefaultClickEventFactory(InternalFactory factory) {
    this.factory = factory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent create(Action action, String value) {
    return this.factory.create(action, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent openUrl(String url) {
    return this.create(Action.OPEN_URL, url);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent openFile(String path) {
    return this.create(Action.OPEN_FILE, path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent runCommand(String command) {
    return this.create(Action.RUN_COMMAND, command);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent suggestCommand(String command) {
    return this.create(Action.SUGGEST_COMMAND, command);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent copyToClipboard(String content) {
    return this.create(Action.COPY_TO_CLIPBOARD, content);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent changeBookPage(int page) {
    return this.create(Action.CHANGE_PAGE, String.valueOf(page));
  }
}
