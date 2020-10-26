package net.labyfy.component.csv.parsing;

import net.labyfy.component.csv.lexical.Token;

import java.util.List;

public interface CSVParser<R> {
    R parse(final List<Token> tokens);
}
