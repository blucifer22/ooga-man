package ooga.view.theme.serialized;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;

/**
 * Primary data container for {@link SerializedTheme}. A {@link SerializedTheme} provides a {@link
 * SerializedCostume} in response to a {@link SerializedTheme#getCostumeForObjectOfType(String)}
 * call; any visual object with a {@link String}-equivalent notion of "type" can thus be themed,
 * should a front-end decide to use themes as provided in the {@link ooga.view.theme} package.
 *
 * @author David Coffman
 */
public class SerializedCostume implements Costume {

  private final Paint fill;
  private final double scale;
  private final boolean bottomHeavy;
  private final boolean rotatable;

  /**
   * Instantiates a {@link SerializedCostume} from a {@link CostumeDescription} (the {@link
   * SerializedCostume}'s JSON-deserialized form).
   *
   * @param description the {@link SerializedCostume}'s JSON-deserialized form
   */
  protected SerializedCostume(CostumeDescription description) {
    this.scale = description.getScale();
    this.bottomHeavy = description.isBottomHeavy();
    this.rotatable = description.isRotatable();

    if (description.isImage()) {
      this.fill = new ImagePattern(new Image(description.getFill()));
    } else {
      this.fill = Color.valueOf(description.getFill().toUpperCase());
    }
  }

  /**
   * Returns the appropriate fill for a themed object in the current theme.
   *
   * @return the appropriate {@link Paint} fill for a themed object  (associated with this {@link
   * Costume}) in the current theme.
   */
  @Override
  public Paint getFill() {
    return this.fill;
  }

  /**
   * Returns the appropriate scale for a themed object in the current theme.
   *
   * @return the appropriate scale for a themed object (associated with this {@link Costume}) in the
   * current theme
   */
  @Override
  public double getScale() {
    return this.scale;
  }

  /**
   * Returns the bottom-heaviness (whether an object <em>horizontal flips</em> instead of
   * <em>rotating</em> for high rotation values to avoid appearing upside-down) for a themed object
   * (associated with this {@link Costume}) in the current theme.
   *
   * @return the bottom-heaviness for a themed object (associated with this {@link Costume}) in the
   * current theme
   */
  @Override
  public boolean isBottomHeavy() {
    return this.bottomHeavy;
  }

  /**
   * Returns a boolean indicating whether a themed object should rotate based on the direction of
   * its travel.
   *
   * @return a boolean indicating whether a themed object (associated with this {@link Costume})
   * should rotate based on the direction of its travel.
   */
  @Override
  public boolean isRotatable() {
    return this.rotatable;
  }
}
