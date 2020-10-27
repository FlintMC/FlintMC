package net.flintmc.util.csv.parsing;

import net.flintmc.util.csv.NamedCSV;
import net.flintmc.util.csv.lexical.Token;
import net.flintmc.util.csv.utils.Pointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NamedCSVParser implements CSVParser<NamedCSV> {
    @Override public NamedCSV parse(final List<Token> tokens) {
        Map<String, List<String>> csv = new HashMap<>();
        Pointer<Integer> columnPointer = new Pointer<>(0);
        Pointer<Boolean> readHeadersPointer = new Pointer<>(false);
        List<String> headers = new ArrayList<>();

        for (Token token : tokens) {
            processToken(csv, columnPointer, readHeadersPointer, headers, token);
        }

        return new NamedCSV(csv);
    }

    private void processToken(Map<String, List<String>> csv,
                              final Pointer<Integer> columnPointer,
                              final Pointer<Boolean> readHeadersPointer,
                              final List<String> headers,
                              final Token token) {
        switch (token.type) {
            case VALUE: {
                if (readHeadersPointer.value) {
                    String header;

                    if (columnPointer.value >= headers.size()) {
                        header = String.valueOf(columnPointer.value++);
                    } else {
                        header = headers.get(columnPointer.value++);
                    }

                    if (!csv.containsKey(header)) {
                        csv.put(header, new ArrayList<>());
                    }

                    csv.get(header).add(token.lexeme);
                } else {
                    headers.add(token.lexeme);
                }
                break;
            }
            case NEWLINE: {
                columnPointer.value = 0;
                readHeadersPointer.value = true;
            }
        }
    }
}
