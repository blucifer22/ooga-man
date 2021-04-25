package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import ooga.model.sprites.Sprite;

/**
 * SpriteLayoutDescription describes the initial layout of all sprites in a Level.
 *
 * <p>Specifically, a SpriteLayoutDescription is a collection of multiple SpriteDescriptions.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class SpriteLayoutDescription extends JSONDescription {

  private final List<SpriteDescription> sprites;

  /**
   * The general constructor for SpriteLayoutDescription. Takes in a List of SpriteDescriptions.
   *
   * @param sprites A List of SpriteDescriptions to add to the layout.
   * @throws IllegalArgumentException If the wrong number of Tiles are provided
   */
  @JsonCreator
  public SpriteLayoutDescription(@JsonProperty("sprites") List<SpriteDescription> sprites)
      throws IllegalArgumentException {
    this.sprites = sprites;
  }

  /**
   * Writes this SpriteLayoutDescription to a JSON file at the indicated filepath.
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
