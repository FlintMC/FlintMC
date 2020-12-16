package net.flintmc.util.commons.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLResolver {

  private URLResolver() {
  }

  /**
   * Opens a {@link HttpURLConnection} and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the resulting connection
   * @throws IOException if the connection could not be established
   */
  public static HttpURLConnection prepare(URL url) throws IOException {
    try {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("User-Agent", "Flint (+https://flintmc.net/)");
      return connection;
    } catch (IOException ex) {
      throw new IOException("Could not establish connection to url " + url, ex);
    }
  }

  /**
   * Opens and connects a {@link HttpURLConnection} and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the resulting connection
   * @throws IOException if the connection could not be established
   */
  public static HttpURLConnection connect(URL url) throws IOException {
    HttpURLConnection connection = prepare(url);
    try {
      connection.connect();
      return connection;
    } catch (IOException ex) {
      throw new IOException("Could not establish connection to url " + url, ex);
    }
  }

  /**
   * Opens an {@link InputStream} to a given url and sets the Flint User Agent.
   *
   * @param url the url to connect to
   * @return the payload
   * @throws IOException if the connection could not be established
   */
  public static InputStream resolve(URL url) throws IOException {
    return connect(url).getInputStream();
  }
}
