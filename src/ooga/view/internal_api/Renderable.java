package ooga.view.internal_api;

import javafx.scene.Node;

/**
 * Standardized interface for rendering views in the frontend. {@link Renderable}-implementing
 * objects declare that they are renderable as scene components (i.e. they manage a renderable
 * {@link javafx.scene.Node}). Used primarily in cases where view components have no "is-a"
 * relationship with an existing JFX component (such classes are declared as extensions of JFX )
 *
 * @author David Coffman
 */
public interface Renderable {

  /**
   * Returns the {@link Renderable}'s managed {@link Node}.
   *
   * @return the {@link Renderable}'s managed {@link Node}.
   */
  Node getRenderingNode();
}
