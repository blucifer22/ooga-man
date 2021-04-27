package ooga.view.theme.api;

import javafx.scene.paint.Paint;

/**
 * Primary data container for {@link Theme}. A {@link Theme} provides a {@link Costume} in
 * response to a {@link Theme#getCostumeForObjectOfType(String)} call; any visual object with a
 * {@link String}-equivalent notion of "type" can thus be themed, should a front-end decide to
 * use themes as provided in the {@link ooga.view.theme} package.
 *
 * @author David Coffman
 */
public interface Costume {

  /**
   * Returns the appropriate fill for a themed object in the current theme.
   *
   * @return the appropriate {@link Paint} fill for a themed object  (associated with this
   * {@link Costume}) in the current theme.
   */
  Paint getFill();

  /**
   * Returns the appropriate scale for a themed object in the current theme.
   *
   * @return the appropriate scale for a themed object (associated with this {@link Costume}) in
   * the current theme
   */
  double getScale();

  /**
   * Returns the bottom-heaviness (whether an object <em>horizontal flips</em> instead of
   * <em>rotating</em> for high rotation values to avoid appearing upside-down) for a themed object
   * (associated with this {@link Costume}) in the current theme.
   *
   * @return the bottom-heaviness for a themed object (associated with this {@link Costume}) in
   * the current theme
   */
  boolean isBottomHeavy();

  /**
   * Returns a boolean indicating whether a themed object should rotate based on the direction of
   * its travel.
   *
   * @return a boolean indicating whether a themed object (associated with this {@link Costume})
   * should rotate based on the direction of its travel.
   */
  boolean isRotatable();
}
