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

package net.flintmc.util.csv.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.flintmc.util.csv.lexical.Token;
import net.flintmc.util.csv.utils.Pointer;

public final class UnnamedCSVParser implements CSVParser<Map<Integer, List<String>>> {

  @Override
  public Map<Integer, List<String>> parse(final List<Token> tokens) {
    Map<Integer, List<String>> csv = new HashMap<>();
    Pointer<Integer> columnPointer = new Pointer<>(0);

    for (Token token : tokens) {
      processToken(csv, columnPointer, token);
    }

    return csv;
  }

  private void processToken(
      Map<Integer, List<String>> csv, final Pointer<Integer> columnPointer, final Token token) {
    switch (token.type) {
      case VALUE: {
        int column = columnPointer.value++;

        if (!csv.containsKey(column)) {
          csv.put(column, new ArrayList<>());
        }

        csv.get(column).add(token.lexeme);
        break;
      }
      case NEWLINE:
        columnPointer.value = 0;
    }
  }
}
