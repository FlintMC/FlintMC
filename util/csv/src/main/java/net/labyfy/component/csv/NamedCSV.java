package net.labyfy.component.csv;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NamedCSV extends CSV<String, String> {
    public NamedCSV(final Map<String, List<String>> data) {
        super(data);
    }

    public Set<String> headers() {
        return data.keySet();
    }
}
