package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.Tile;

/**
 * GridDescription contains all of the information required to construct an ObservableGrid (AKA: A
 * Pac-Man grid) including width, height, and Sprites.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class GridDescription extends JSONDescription {

  private final String gridName;
  private final int width;
  private final int height;
  private final List<List<Tile>> grid;

  /**
   * The general constructor for GridDescription. Takes in a width, a height, and a 2D array of
   * Tiles with which to initially populate itself.
   *
   * @param gridName The name of the Grid that this GridDescription represents.
   * @param width The width of the Grid that this GridDescription represents.
   * @param height The height of the Grid that this GridDescription represents.
   * @param tileList The List of Tiles that compose the Grid that this GridDescription represents.
   * @throws IllegalArgumentException If the wrong number of Tiles are provided
   */
  @JsonCreator
  public GridDescription(
      @JsonProperty("gridName") String gridName,
      @JsonProperty("width") int width,
      @JsonProperty("height") int height,
      @JsonProperty("grid") List<List<Tile>> tileList)
      throws IllegalArgumentException {

    this.gridName = gridName;
    this.width = width;
    this.height = height;

    if (tileList.get(0).size() != width || tileList.size() != height) {
      throw new IllegalArgumentException(
          "ILLEGAL ARGUMENT EXCEPTION:\nWRONG NUMBER OF TILES FOR INDICATED DIMENSIONS!");
    }

    this.grid = tileList;
  }

  /**
   * A special-case constructor for GridDescription that takes in a PacmanGrid and constructs a grid
   * description based off of its paramters. Specifically, it utilizes the PacmanGrid's width, height,
   * and tiles, and passes in a blank String "" for name as that is unnecessary in this schema.
   *
   * @param grid The PacmanGrid from which to construct a GridDescription
   */
  public GridDescription(PacmanGrid grid) {
    this("", grid.getWidth(), grid.getHeight(), grid.getAllTiles());
  }

  /**
   * Get the name of this GridDescription.
   *
   * @return The name of this GridDescription.
   */
  @JsonGetter
  public String getGridName() {
    return gridName;
  }

  /**
   * Get the width of this GridDescription.
   *
   * @return The width of this GridDescription.
   */
  @JsonGetter
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of this GridDescription.
   *
   * @return The height of this GridDescription.
   */
  @JsonGetter
  public int getHeight() {
    return height;
  }

  /**
   * Get the 2D array of Tiles that make up this GridDescription
   *
   * @return A 2D array of Tiles that make up this GridDescription
   */
  @JsonGetter
  public List<List<Tile>> getGrid() {
    return grid;
  }

  /**
   * This method allows for this GridDescription to be converted into a PacmanGrid by utilizing
   * reflection.
   *
   * @return A PacmanGrid that possesses the properties of this GridDescription.
   */
  public PacmanGrid toGrid() {
    try {
      Class<?> spriteClass = Class.forName("ooga.model.grid.PacmanGrid");
      return (PacmanGrid)
          spriteClass.getDeclaredConstructor(GridDescription.class).newInstance(this);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InstantiationException
        | InvocationTargetException
        | IllegalAccessException e) {
    }
    return null;
  }
}
