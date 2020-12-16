package net.flintmc.framework.config.internal.transform;

import java.util.Collection;

public interface ConfigTransformer {

  Collection<TransformedConfigMeta> getMappings();

  Collection<PendingTransform> getPendingTransforms();
}
