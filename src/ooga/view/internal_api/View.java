package ooga.view.internal_api;

import javafx.scene.layout.Pane;

/**
 * Strengthening of the {@link Renderable} interface used to render <em>whole views</em> rather than
 * view components. See {@link Renderable}.
 *
 * @author David Coffman
 */
public interface View extends Renderable {

  /**
   * Returns the {@link View}'s managed {@link Pane}. Stricter requirement than that of the
   * overridden method in {@link Renderable}, which guarantees only a {@link javafx.scene.Node}.
   *
   * @return the {@link View}'s managed {@link Pane}.
   */
  @Override
  Pane getRenderingNode();
}
