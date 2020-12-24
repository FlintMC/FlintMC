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

package net.flintmc.util.csv.lexical;

import java.util.Objects;

public final class Token {

  public final TokenType type;
  public final String lexeme;

  /**
   * Construct a token.
   *
   * @param type   a token type.
   * @param lexeme a lexeme.
   */
  Token(final TokenType type, final String lexeme) {
    this.type = Objects.requireNonNull(type, "type must not be null");
    this.lexeme = Objects.requireNonNull(lexeme, "lexeme must not be null");
  }

  @Override
  public String toString() {
    return "Token{" + "type=" + type + ", lexeme='" + lexeme + '\'' + '}';
  }
}
