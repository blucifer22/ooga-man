package ooga.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * GridDescription contains all of the information required to construct an ObservableGrid (AKA: A
 * Pac-Man level) including width, height, and Sprites.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class GridDescription {

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

    if (tileList.size() != width || tileList.get(0).size() != height) {
      throw new IllegalArgumentException(
          "ILLEGAL ARGUMENT EXCEPTION:\nWRONG NUMBER OF TILES FOR INDICATED DIMENSIONS!");
    }

    this.grid = tileList;
  }

  /**
   * Writes this GridDescription to a JSON file at the indicated filepath.
   *
   * @param filepath The filepath at which to write the JSON.
   * @throws IOException If the provided filepath is invalid.
   */
  public void toJSON(String filepath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(new File(filepath), this);
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
}
