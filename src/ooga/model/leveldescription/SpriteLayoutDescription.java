package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ooga.model.Tile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * SpriteLayoutDescription describes the initial layout of all sprites
 * in a LevelDescription.
 *
 * Specifically, a SpriteLayoutDescription is a collection of multiple
 * SpriteDescriptions.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class SpriteLayoutDescription extends JSONDescription {

  private final List<SpriteDescription> sprites;

  /**
   * The general constructor for SpriteL. Takes in a width, a height, and a 2D array of
   * Tiles with which to initially populate itself.
   *
   * @param gridName The name of the Grid that this GridDescription represents.
   * @param width The width of the Grid that this GridDescription represents.
   * @param height The height of the Grid that this GridDescription represents.
   * @param tileList The List of Tiles that compose the Grid that this GridDescription represents.
   * @throws IllegalArgumentException If the wrong number of Tiles are provided
   */
  @JsonCreator
  public SpriteLayoutDescription(
      @JsonProperty("sprites") List<SpriteDescription> sprites)
      throws IllegalArgumentException {
    this.sprites = sprites;
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
   * Get the list of sprite descriptions contained in this layout.
   *
   * @return The list of sprite descriptions.
   */
  @JsonGetter
  public List<SpriteDescription> getSprites() {
    return sprites;
  }
}
