package ooga.view;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;

public class GameView implements SpriteExistenceObserver, Renderable {

  private final Map<SpriteObservable, SpriteView> views;
  private final Group primaryView;
  private final Group sprites;

  public GameView() {
    this.views = new HashMap<>();
    this.sprites = new Group();
    GameGridView backgroundGrid = new GameGridView(0, 0); // TODO: fix me
    this.primaryView = new Group(this.sprites, backgroundGrid.getRenderingNode());
  }

  @Override
  public void onSpriteCreation(SpriteObservable so) {
    SpriteView createdSpriteView = new SpriteView(so);
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
