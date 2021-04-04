package ooga.view;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;

public class GameView implements SpriteExistenceObserver, Renderable {

  private final Map<SpriteObservable, SpriteView> views;
  private final GridPane primaryView;
  private final Group sprites;
  private final DoubleProperty tileSize;

  public GameView(int rows, int cols) {
    // Track existing Sprites with Map; render them with Group
    this.views = new HashMap<>();
    this.sprites = new Group();

    // Lay out grid
    GameGridView backgroundGrid = new GameGridView(rows, cols);
    StackPane overlay = new StackPane(backgroundGrid.getRenderingNode(), this.sprites);
    this.tileSize = new SimpleDoubleProperty(0);

    this.primaryView = new GridPane();
    GridPane.setConstraints(overlay, 0, 0);
    primaryView.getChildren().add(overlay);

    RowConstraints rc = new RowConstraints();
    rc.setVgrow(Priority.ALWAYS);
    ColumnConstraints cc = new ColumnConstraints();
    cc.setHgrow(Priority.ALWAYS);

    primaryView.getRowConstraints().add(rc);
    primaryView.getColumnConstraints().add(cc);

    this.tileSize.bind(backgroundGrid.tileSizeProperty());
  }

  @Override
  public void onSpriteCreation(SpriteObservable so) {
    SpriteView createdSpriteView = new SpriteView(so, tileSize);
    System.out.println(tileSize);
    views.put(so, createdSpriteView);
    sprites.getChildren().add(createdSpriteView.getRenderingNode());
  }

  @Override
  public void onSpriteDestruction(SpriteObservable so) {
    sprites.getChildren().remove(views.get(so).getRenderingNode());
    views.remove(so);
  }

  @Override
  public Node getRenderingNode() {
    return this.primaryView;
  }
}
