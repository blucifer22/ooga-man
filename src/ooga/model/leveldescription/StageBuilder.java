package ooga.model.leveldescription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGrid;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.api.GameStateObserver;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.PowerupEventObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.sprites.Sprite;

/**
 * The StageBuilder is a stripped-down, non-steppable version of Pac-Man game state.
 *
 * @author George Hong
 */
public class StageBuilder implements SpriteExistenceObservable, GridRebuildObservable {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<Sprite> sprites;
  private String jsonFileName;
  private PacmanGrid grid;

  public StageBuilder() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    sprites = new HashSet<>();
  }

  /**
   * Generates the Grid, allowing for default values to be set.  The Grid Builder assumes a default
   * open grid, with a frame preventing any Sprites from leaving
   *
   * @param height height of the grid
   * @param width  width of the grid
   */
  public void setGridSize(int height, int width) {
    grid = new PacmanGrid(height, width);
    List<List<Tile>> tileList = new ArrayList<>();
    for (int y = 0; y < height; y++) {
      List<Tile> outputRow = new ArrayList<>();
      for (int x = 0; x < width; x++) {
        boolean openTile = x != 0 && y != 0 && x != width - 1 && y != height - 1;
        String mazeTile =
            x != 0 && y != 0 && x != width - 1 && y != height - 1 ? "tile" : "tileclosed";
        outputRow.add(new Tile(new TileCoordinates(x, y), mazeTile, openTile, openTile));
      }
      tileList.add(outputRow);
    }
  }


  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }

  @Override
  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

}
