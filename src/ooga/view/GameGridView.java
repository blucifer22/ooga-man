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
    ColumnConstraints flexCol = new ColumnConstraints();
    flexCol.setHgrow(Priority.ALWAYS);
    flexCol.setFillWidth(true);
    ColumnConstraints cc = new ColumnConstraints();
    cc.setHgrow(Priority.NEVER);
    tileGrid.getColumnConstraints().add(flexCol);
    for (int i = 0; i < cols; i++) {
      tileGrid.getColumnConstraints().add(cc);
    }
    tileGrid.getColumnConstraints().add(flexCol);

    cc.prefWidthProperty().bind(tileSizeProperty);
  }

  private void configureRowConstraints(int rows) {
    RowConstraints flexRow = new RowConstraints();
    flexRow.setVgrow(Priority.ALWAYS);
    flexRow.setFillHeight(true);
    RowConstraints rc = new RowConstraints();
    rc.setVgrow(Priority.NEVER);
    tileGrid.getRowConstraints().add(flexRow);
    for (int i = 0; i < rows; i++) {
      tileGrid.getRowConstraints().add(rc);
    }
    tileGrid.getRowConstraints().add(flexRow);

    rc.prefHeightProperty().bind(tileSizeProperty());
  }

  private void addGridTiles(int rows, int cols) {
    for (int i = 1; i < rows+1; i++) {
      for (int j = 1; j < cols+1; j++) {
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