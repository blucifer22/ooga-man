package ooga.view.theme.serialized;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;

public class DeserializedCostume implements Costume {

  private final Paint fill;
  private final double scale;
  private final boolean bottomHeavy;

  protected DeserializedCostume(CostumeDescription description) {
    this.scale = description.getScale();
    this.bottomHeavy = description.isBottomHeavy();

    if (description.isImage()) {
      this.fill = Color.valueOf(description.getFill().toUpperCase());
    } else {
      this.fill = new ImagePattern(new Image(description.getFill()));
    }
  }

  @Override
  public Paint getFill() {
    return this.fill;
  }

  @Override
  public double getScale() {
    return this.scale;
  }

  @Override
  public boolean isBottomHeavy() {
    return this.bottomHeavy;
  }
}
