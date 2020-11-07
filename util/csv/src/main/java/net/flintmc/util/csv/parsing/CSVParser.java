package net.flintmc.util.csv.parsing;

import net.flintmc.util.csv.lexical.Token;

import java.util.List;

public interface CSVParser<R> {
  R parse(final List<Token> tokens);
}
