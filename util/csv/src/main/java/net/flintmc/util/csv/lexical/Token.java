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
