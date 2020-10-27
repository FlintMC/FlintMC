package net.flintmc.util.csv.lexical;

import net.flintmc.util.csv.utils.Pointer;

import java.util.ArrayList;
import java.util.List;

public final class Tokenizer {
    private final char separator;

    /**
     * Construct a tokenizer.
     *
     * @param separator  a CSV separator.
     */
    public Tokenizer(final char separator) {
        this.separator = separator;
    }

    /**
     * Tokenize the input.
     *
     * @param input raw CSV data.
     * @return a list of tokens.
     */
    public List<Token> tokenize(final String input) {
        List<Token> tokens = new ArrayList<>();
        Pointer<Integer> start = new Pointer<>(0);
        Pointer<Integer> current = new Pointer<>(0);

        while (!isAtEnd(current.value, input)) {
            processNextToken(tokens, start, current, input);
            start.value = current.value;
        }

        return tokens;
    }

    // Extracted methods

    private void processNextToken(List<Token> tokens,
                                  final Pointer<Integer> start,
                                  final Pointer<Integer> current,
                                  final String input) {
        char character = advance(current, input);

        // We cannot put this in the switch statement as separator is not a constant.
        if (character == separator) {
            return;
        }

        switch (character) {
            case '"': processQuotation(tokens, current, input); break;
            case '\r': break;
            case '\n': tokens.add(new Token(TokenType.NEWLINE, "\r\n")); break;
            default: processLiteral(tokens, start, current, input);
        }
    }

    private void processQuotation(List<Token> tokens,
                                  final Pointer<Integer> current,
                                  final String input) {
        StringBuilder builder = new StringBuilder();

        while (!isAtEnd(current.value, input)) {
            if (peek(current.value, input) == '"') {
                // Check whether or not we must skip the next quote character due to quotation escaping described in
                // RFC 4180 2.7.
                if (!isAtEnd(current.value, input, 1) && peek(current.value, input, 1) == '"') {
                    current.value++;
                } else {
                    break;
                }
            }

            builder.append(peek(current.value, input));
            current.value++;
        }

        // TODO: Throw custom exception here.
        if (isAtEnd(current.value, input)) throw new RuntimeException("Unterminated string");

        tokens.add(new Token(TokenType.VALUE, builder.toString()));
        current.value++;
    }

    private void processLiteral(List<Token> tokens,
                                final Pointer<Integer> start,
                                final Pointer<Integer> current,
                                final String input) {
        while (!isAtEnd(current.value, input) && peek(current.value, input) != separator && peek(current.value, input) != '\n') {
            current.value++;
        }

        tokens.add(new Token(TokenType.VALUE, input.substring(start.value, current.value)));
    }

    // Utilities

    /**
     * Get the next character.
     *
     * @return next character.
     */
    private char advance(final Pointer<Integer> current, final String input) {
        return input.charAt(current.value++);
    }

    private char peek(int current, final String input) {
        return peek(current, input, 0);
    }

    /**
     * Peek ahead.
     *
     * @param offset additional offset.
     * @return character at current + offset + 1.
     */
    private char peek(int current, final String input, int offset) {
        // We increment current in #advance already.
        return input.charAt(current + offset);
    }

    // State

    private boolean isAtEnd(int current, final String input) {
        return isAtEnd(current, input, 0);
    }

    /**
     * Check whether or not input is exhausted.
     *
     * @param offset additional offset.
     * @return true, input is exhausted. False otherwise.
     */
    private boolean isAtEnd(int current, final String input, int offset) {
        return current + offset >= input.length();
    }
}
