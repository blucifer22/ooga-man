package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.InvocationTargetException;
import ooga.model.sprites.Sprite;
import ooga.model.SpriteCoordinates;

/**
 * A class that serializes Sprites to JSON and can use reflection to convert it back to a Sprite.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class SpriteDescription extends JSONDescription {
  /**
   * Name of the class (which must extend ooga.model.sprites.Sprite) backing this sprite.
   */
  private final String spriteClassName;

  /**
   * String constant representing the source of this sprite's input commands (if any).
   *
   * <p>Allowed values: "BlinkyAI, PinkyAI, InkyAI, etc."
   */
  private final String inputSource;

  private final SpriteCoordinates coordinates;

  @JsonCreator
  public SpriteDescription(
      @JsonProperty("className") String className,
      @JsonProperty("inputSource") String inputSource,
      @JsonProperty("startLocation") SpriteCoordinates coordinates)
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
  public SpriteCoordinates getCoordinates() {
    return coordinates;
  }

  public Sprite toSprite() {
    try {
      Class<?> spriteClass = Class.forName("ooga.model.sprites." + spriteClassName);
      return (Sprite) spriteClass.getDeclaredConstructor(SpriteDescription.class).newInstance(this);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InstantiationException
        | InvocationTargetException
        | IllegalAccessException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
    return null;
  }
}
