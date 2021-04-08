package ooga.view.theme;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

public class SerializedCostume implements Costume {

  private Paint fill;
  private double scale;
  private boolean bottomHeavy;

  public SerializedCostume(CostumeDescription description) {
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
    return null;
  }

  @Override
  public double getScale() {
    return 0;
  }

  @Override
  public boolean isBottomHeavy() {
    return false;
  }
}
