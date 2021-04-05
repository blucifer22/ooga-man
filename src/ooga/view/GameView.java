package ooga.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import ooga.model.SpriteExistenceObserver;

/**
 * GameView lays out how a round appears (the GridView in the center, information about
 * lives/round/score above and below).
 */
public class GameView implements Renderable {

  private final GridPane primaryView;
  private final GameGridView gridView;

  public GameView(int rows, int cols) {
    this.primaryView = new GridPane();
    this.gridView = new GameGridView(rows, cols);

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(80);

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(80);

    this.primaryView.getRowConstraints().add(rc);
    this.primaryView.getColumnConstraints().add(cc);
    this.primaryView.add(this.gridView.getRenderingNode(), 0, 0);
    this.primaryView.setAlignment(Pos.CENTER);
  }

  public SpriteExistenceObserver getSpriteExistenceObserver() {
    return this.gridView;
  }

  @Override
  public Node getRenderingNode() {
    return this.primaryView;
  }
}
