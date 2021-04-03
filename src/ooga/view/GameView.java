package ooga.view;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;

public class GameView implements SpriteExistenceObserver, Renderable {

  private final Map<SpriteObservable, SpriteView> views;
  private final StackPane primaryView;
  private final Group sprites;
  private final DoubleProperty tileSize;

  public GameView(int rows, int cols) {
    // Track existing Sprites with Map; render them with Group
    this.views = new HashMap<>();
    this.sprites = new Group();

    // Lay out grid
    GameGridView backgroundGrid = new GameGridView(rows, cols);
    this.primaryView = new StackPane(backgroundGrid.getRenderingNode(), this.sprites);
    this.tileSize = new SimpleDoubleProperty(0);
    this.tileSize.bind(this.primaryView.widthProperty().divide(cols));
  }

  @Override
  public void onSpriteCreation(SpriteObservable so) {
    SpriteView createdSpriteView = new SpriteView(so, tileSize);
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
