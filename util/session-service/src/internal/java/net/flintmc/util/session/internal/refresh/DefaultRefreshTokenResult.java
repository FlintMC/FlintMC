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

package net.flintmc.util.session.internal.refresh;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.RefreshTokenResult;

@Implement(RefreshTokenResult.class)
public class DefaultRefreshTokenResult implements RefreshTokenResult {

  private final ResultType type;
  private final String errorMessage;

  @AssistedInject
  private DefaultRefreshTokenResult(@Assisted("type") ResultType type) {
    this(type, null);
  }

  @AssistedInject
  private DefaultRefreshTokenResult(
      @Assisted("type") ResultType type, @Assisted("errorMessage") String errorMessage) {
    this.type = type;
    this.errorMessage = errorMessage;
  }

  @Override
  public ResultType getType() {
    return this.type;
  }

  @Override
  public String getErrorMessage() {
    return this.errorMessage;
  }
}
