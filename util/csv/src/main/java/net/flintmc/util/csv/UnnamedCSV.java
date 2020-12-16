package net.flintmc.util.csv;

import java.util.List;
import java.util.Map;

public final class UnnamedCSV extends CSV<Integer, String> {

  public UnnamedCSV(final Map<Integer, List<String>> data) {
    super(data);
  }
}
