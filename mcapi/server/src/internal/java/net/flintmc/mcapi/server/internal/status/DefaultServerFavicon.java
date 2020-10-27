package net.flintmc.mcapi.server.internal.status;

import com.google.common.base.Charsets;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.status.ServerFavicon;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Implement(ServerFavicon.class)
public class DefaultServerFavicon implements ServerFavicon {

  public static final String PREFIX = "data:image/png;base64,";

  private final Logger logger;
  private final ResourceLocation defaultServerIcon;
  private final byte[] data;

  @AssistedInject
  public DefaultServerFavicon(@InjectLogger Logger logger, @Assisted("resourceLocation") ResourceLocation defaultServerIcon) {
    this(logger, defaultServerIcon, null);
  }

  @AssistedInject
  public DefaultServerFavicon(@Assisted("base64Data") String base64Data) throws IllegalArgumentException {
    this(Base64.getDecoder()
        .decode(base64Data.substring(PREFIX.length()).replaceAll("\n", "").getBytes(Charsets.UTF_8))
    );
  }

  @AssistedInject
  public DefaultServerFavicon(@Assisted("data") byte[] data) {
    this(null, null, data);
  }

  private DefaultServerFavicon(Logger logger, ResourceLocation defaultServerIcon, byte[] data) {
    this.logger = logger;
    this.defaultServerIcon = defaultServerIcon;
    this.data = data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCustom() {
    return this.data != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream createStream() {
    if (this.data == null) {
      try {
        return this.defaultServerIcon.openInputStream();
      } catch (IOException exception) {
        this.logger.trace("Exception while loading the default server icon", exception);
        return null;
      }
    }

    return new ByteArrayInputStream(this.data);
  }

}
