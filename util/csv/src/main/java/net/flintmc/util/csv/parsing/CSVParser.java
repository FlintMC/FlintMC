package net.flintmc.util.csv.parsing;

import java.util.List;
import net.flintmc.util.csv.lexical.Token;

public interface CSVParser<R> {

  R parse(final List<Token> tokens);
}
