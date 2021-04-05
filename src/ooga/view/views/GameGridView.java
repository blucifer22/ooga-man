package ooga.view.views;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

/**
 * GameGridView lays out the grid and the Sprites on the grid (a necessary combination because only
 * the GameGridView knows where the grid is!).
 */
public class GameGridView implements Renderable, SpriteExistenceObserver, ThemedObject {

  private final Group tileGrid;
  private final DoubleProperty tileSize;
  private final HashMap<SpriteObservable, SpriteView> spriteViews;
  private final Group spriteNodes;
  private final Pane primaryView;
  private ThemeService themeService;

  public GameGridView(int rows, int cols, ThemeService themeService) {
    this.primaryView = new Pane();
    this.tileGrid = new Group();
    this.tileSize = new SimpleDoubleProperty();
    this.tileSize.bind(Bindings.min(primaryView.widthProperty().divide(cols),
        primaryView.heightProperty().divide(rows)));

    this.spriteViews = new HashMap<>();
    this.spriteNodes = new Group();

    this.primaryView.getChildren().addAll(tileGrid, spriteNodes);

    setThemeService(themeService);

    addGridTiles(rows, cols);
  }

  private void addGridTiles(int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Rectangle r = new Rectangle(0, 0, 0, 0);
        r.widthProperty().bind(tileSize);
        r.heightProperty().bind(tileSize);
        r.layoutXProperty().bind(tileSize.multiply(j));
        r.layoutYProperty().bind(tileSize.multiply(i));
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.PINK);
        r.setStrokeWidth(1.0);
        r.setStrokeType(StrokeType.INSIDE);
        tileGrid.getChildren().add(r);
      }
    }
  }

  @Override
  public void onSpriteCreation(SpriteObservable so) {
    SpriteView createdSpriteView = new SpriteView(so, this.themeService, tileSize);
    spriteViews.put(so, createdSpriteView);
    spriteNodes.getChildren().add(createdSpriteView.getRenderingNode());
  }

  @Override
  public void onSpriteDestruction(SpriteObservable so) {
    spriteNodes.getChildren().remove(spriteViews.get(so).getRenderingNode());
    spriteViews.remove(so);
  }

  @Override
  public Node getRenderingNode() {
    return primaryView;
  }

  @Override
  public void onThemeChange() {
    // TODO: re-skin tiles!
  }

  @Override
  public void setThemeService(ThemeService themeService) {
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
  }
}