package net.flintmc.util.mojang.internal.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

@Singleton
@Service(CacheIO.class)
public class CacheService implements ServiceHandler<CacheIO> {

  private final FileCache cache;

  @Inject
  private CacheService(FileCache cache) {
    this.cache = cache;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void discover(AnnotationMeta<CacheIO> meta) {
    CachedObjectIO io =
        InjectionHolder.getInjectedInstance(
            CtResolver.get(meta.getClassIdentifier().getLocation()));
    this.cache.registerIO(io.getType(), io);
  }
}
