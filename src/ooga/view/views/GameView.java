package ooga.view.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ooga.model.SpriteExistenceObserver;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

/**
 * GameView lays out how a round appears (the GridView in the center, information about
 * lives/round/score above and below).
 */
public class GameView implements Renderable {

  private final GridPane primaryView;
  private final GameGridView gridView;
  private ThemeService themeService;

  public GameView(int rows, int cols) {
    this.primaryView = new GridPane();

    this.themeService = new ThemeService() {
      @Override
      public Paint getFillForObjectOfType(String type) {
        return Color.BLUE;
      }

      @Override
      public void addThemedObject(ThemedObject themedObject) {
      }
    };

    this.gridView = new GameGridView(rows, cols, this.themeService);

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(80);

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(80);

    this.primaryView.getRowConstraints().add(rc);
    this.primaryView.getColumnConstraints().add(cc);
    this.primaryView.add(this.gridView.getRenderingNode(), 0, 0);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY,
        Insets.EMPTY)));
  }

  public SpriteExistenceObserver getSpriteExistenceObserver() {
    return this.gridView;
  }

  @Override
  public Node getRenderingNode() {
    return this.primaryView;
  }
}
