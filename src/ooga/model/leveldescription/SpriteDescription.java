package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.TileCoordinates;

public class SpriteDescription extends JSONDescription {
  /**
   * Fully qualified name of the class (which must extend ooga.model.Sprite) backing this sprite.
   */
  private final String spriteClassName;

  /**
   * String constant representing the source of this sprite's input commands (if any).
   *
   * <p>Allowed values: "HUMAN", "GHOST_AI", "PACMAN_AI", "NONE"
   */
  private final String inputSource;

  private final TileCoordinates coordinates;

  @JsonCreator
  public SpriteDescription(
      @JsonProperty("class") String className,
      @JsonProperty("inputSource") String inputSource,
      @JsonProperty("startLocation") TileCoordinates coordinates)
      throws IllegalArgumentException {
    this.spriteClassName = className;
    this.inputSource = inputSource;
    this.coordinates = coordinates;
  }

  @JsonGetter
  public String getClassName() {
    return spriteClassName;
  }

  @JsonGetter
  public String getInputSource() {
    return inputSource;
  }

  @JsonGetter
  public TileCoordinates getCoordinates() {
    return coordinates;
  }
}
