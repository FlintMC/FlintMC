package net.flintmc.util.csv.parsing;

import net.flintmc.util.csv.lexical.Token;
import net.flintmc.util.csv.utils.Pointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UnnamedCSVParser implements CSVParser<Map<Integer, List<String>>> {
    @Override public Map<Integer, List<String>> parse(final List<Token> tokens) {
        Map<Integer, List<String>> csv = new HashMap<>();
        Pointer<Integer> columnPointer = new Pointer<>(0);

        for (Token token : tokens) {
            processToken(csv, columnPointer, token);
        }

        return csv;
    }

    private void processToken(Map<Integer, List<String>> csv, final Pointer<Integer> columnPointer, final Token token) {
        switch (token.type) {
            case VALUE: {
                int column = columnPointer.value++;

                if (!csv.containsKey(column)) {
                    csv.put(column, new ArrayList<>());
                }

                csv.get(column).add(token.lexeme);
                break;
            }
            case NEWLINE: columnPointer.value = 0;
        }
    }
}
