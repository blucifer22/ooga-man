package ooga.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class GameGridView implements Renderable {

  private final GridPane tileGrid;
  private final DoubleProperty tileSizeProperty;

  public GameGridView(int rows, int cols) {
    this.tileGrid = new GridPane();

    this.tileSizeProperty = new SimpleDoubleProperty(0);
    this.tileSizeProperty.bind(Bindings.min(tileGrid.widthProperty().divide(cols),
        tileGrid.heightProperty().divide(rows)));

    configureRowConstraints(rows);
    configureColumnConstraints(cols);
    addGridTiles(rows, cols);
  }

  private void configureColumnConstraints(int cols) {
    ColumnConstraints cc = new ColumnConstraints();
    cc.setHgrow(Priority.NEVER);
    for (int i = 0; i < cols; i++) {
      tileGrid.getColumnConstraints().add(cc);
    }
    cc.prefWidthProperty().bind(tileSizeProperty);
  }

  private void configureRowConstraints(int rows) {
    RowConstraints rc = new RowConstraints();
    rc.setVgrow(Priority.NEVER);
    for (int i = 0; i < rows; i++) {
      tileGrid.getRowConstraints().add(rc);
    }
    rc.prefHeightProperty().bind(tileSizeProperty());
  }

  private void addGridTiles(int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle r = new Rectangle(0, 0, 0, 0);
        r.widthProperty().bind(tileSizeProperty);
        r.heightProperty().bind(tileSizeProperty);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.PINK);
        r.setStrokeWidth(1.0);
        r.setStrokeType(StrokeType.INSIDE);
        GridPane.setConstraints(r, j, i);
        tileGrid.getChildren().add(r);
      }
    }
  }

  public ReadOnlyDoubleProperty tileSizeProperty() {
    return this.tileSizeProperty;
  }

  @Override
  public Node getRenderingNode() {
    return tileGrid;
  }
}