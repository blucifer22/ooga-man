package ooga.view;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
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
    this.sprites.getChildren().add(new Rectangle(0,0,0,0));

    // Lay out grid
    GameGridView backgroundGrid = new GameGridView(rows, cols);
    StackPane layeredView = new StackPane(backgroundGrid.getRenderingNode(), this.sprites);
    layeredView.setAlignment(Pos.TOP_LEFT);
    this.tileSize = new SimpleDoubleProperty(0);
    this.tileSize.bind(backgroundGrid.tileSizeProperty());

    this.primaryView = new GridPane();

    RowConstraints flexRow = new RowConstraints();
    flexRow.setVgrow(Priority.ALWAYS);

    RowConstraints fixedRow = new RowConstraints();
    fixedRow.setVgrow(Priority.NEVER);
    fixedRow.prefHeightProperty().bind(this.primaryView.heightProperty());

    ColumnConstraints flexCol = new ColumnConstraints();
    flexCol.setHgrow(Priority.ALWAYS);

    ColumnConstraints fixedCol = new ColumnConstraints();
    fixedCol.setHgrow(Priority.NEVER);
    fixedCol.prefWidthProperty().bind(this.primaryView.widthProperty());

    this.primaryView.getRowConstraints().addAll(flexRow, fixedRow, flexRow);
    this.primaryView.getColumnConstraints().addAll(flexCol, fixedCol, flexCol);

    GridPane.setConstraints(layeredView, 1, 1);

    this.primaryView.getChildren().add(layeredView);

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
