package net.labyfy.component.gui.mcjfxgl.component.style.css.animate;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.concurrent.atomic.AtomicBoolean;

public class PropertyAnimationCacheMemento {

  private boolean cache;
  private boolean cacheShape;
  private boolean snapToPixel;
  private CacheHint cacheHint;
  private Node node;
  private AtomicBoolean isCached;

  public PropertyAnimationCacheMemento(Node node) {
    this.node = node;
    this.cacheHint = CacheHint.DEFAULT;
    this.isCached = new AtomicBoolean(false);
  }

  public void cache() {
    if (!isCached.getAndSet(true)) {
      this.cache = node.isCache();
      this.cacheHint = node.getCacheHint();
      node.setCache(true);
      node.setCacheHint(CacheHint.SPEED);
      if (node instanceof Region) {
        this.cacheShape = ((Region) node).isCacheShape();
        this.snapToPixel = ((Region) node).isSnapToPixel();
        ((Region) node).setCacheShape(true);
        ((Region) node).setSnapToPixel(true);
      }
    }
  }

  public void restore() {
    if (isCached.getAndSet(false)) {
      node.setCache(cache);
      node.setCacheHint(cacheHint);
      if (node instanceof Region) {
        ((Region) node).setCacheShape(cacheShape);
        ((Region) node).setSnapToPixel(snapToPixel);
      }
    }
  }
}
