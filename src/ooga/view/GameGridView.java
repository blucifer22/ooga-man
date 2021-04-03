package ooga.view;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;

public class GameGridView implements SpriteExistenceObserver, Renderable {

  private final Map<SpriteObservable, SpriteView> views;
  private final GridPane gameGrid;

  public GameGridView(int rows, int cols) {
    this.views = new HashMap<>();
    this.gameGrid = new GridPane();

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(100.0/rows);
    for (int i = 0; i < rows; i++) {
      gameGrid.getRowConstraints().add(rc);
    }

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(100.0/cols);
    for (int i = 0; i < cols; i++) {
      gameGrid.getColumnConstraints().add(cc);
    }
  }

  public void onSpriteCreation(SpriteObservable so) {
    SpriteView sv = new SpriteView(so);
    views.put(so, sv);
    // render the SpriteView
  }

  public void onSpriteDestruction(SpriteObservable so) {
    gameGrid.getChildren().remove(views.get(so).getRenderingNode());
    views.remove(so);
    // de-render the SpriteView
  }

  @Override
  public Node getRenderingNode() {
    return gameGrid;
  }
}