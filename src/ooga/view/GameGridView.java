package ooga.view;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameGridView implements Renderable {

  private final GridPane tileGrid;

  public GameGridView(int rows, int cols) {
    this.tileGrid = new GridPane();

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(100.0/rows);
    for (int i = 0; i < rows; i++) {
      tileGrid.getRowConstraints().add(rc);
    }

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(100.0/cols);
    for (int i = 0; i < cols; i++) {
      tileGrid.getColumnConstraints().add(cc);
    }
  }

  @Override
  public Node getRenderingNode() {
    return tileGrid;
  }
}