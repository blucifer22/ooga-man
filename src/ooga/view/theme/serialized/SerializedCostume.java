package ooga.view.theme.serialized;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;

public class SerializedCostume implements Costume {

  private final Paint fill;
  private final double scale;
  private final boolean bottomHeavy;
  private final boolean rotatable;

  protected SerializedCostume(CostumeDescription description) {
    this.scale = description.getScale();
    this.bottomHeavy = description.isBottomHeavy();
    this.rotatable = description.isRotatable();

    if (description.isImage()) {
      System.out.println(description.getFill());
      this.fill = new ImagePattern(new Image(description.getFill()));
    } else {
      this.fill = Color.valueOf(description.getFill().toUpperCase());
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

  @Override
  public boolean isRotatable() {
    return this.rotatable;
  }
}
