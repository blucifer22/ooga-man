package ooga.view.theme;

import javafx.scene.paint.Paint;

public class SerializedCostume implements Costume {

  private Paint fill;
  private double scale;
  private boolean bottomHeavy;

  public SerializedCostume(CostumeDescription description) {

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
