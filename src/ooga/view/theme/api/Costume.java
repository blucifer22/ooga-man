package ooga.view.theme.api;

import javafx.scene.paint.Paint;

public interface Costume {

  Paint getFill();

  double getScale();

  boolean isBottomHeavy();

  boolean isRotatable();
}
