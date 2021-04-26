package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.InvocationTargetException;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.Sprite;

/**
 * A class that serializes Sprites to JSON and can use reflection to convert it back to a Sprite.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class SpriteDescription extends JSONDescription {
  /** Name of the class (which must extend ooga.model.sprites.Sprite) backing this sprite. */
  private final String spriteClassName;

  /**
   * String constant representing the source of this sprite's input commands (if any).
   *
   * <p>Allowed values: "BlinkyAI, PinkyAI, InkyAI, etc."
   */
  private final String inputSource;

  private final SpriteCoordinates coordinates;

  /**
   * The general-case constructor for SpriteDescription. Takes in a className, inputSource, and
   * coordinates and constructs a new SpriteDescription from that information. Contains Jackson JSON
   * serialization annotations.
   *
   * @param className The className of the Sprite being described.
   * @param inputSource The inputSource of the Sprite being described.
   * @param coordinates The location of the Sprite being described.
   * @throws IllegalArgumentException In the event that an IllegalArgument is passed to the
   *     constructor
   */
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

  /**
   * An alternative constructor for SpriteDescription that takes in a Sprite and pulls out the
   * relevant information from it which is required to describe it. This is the used to call the
   * general-case constructor to construct the SpriteDescription.
   *
   * @param sprite The Sprite from which to construct a SpriteDescription
   */
  public SpriteDescription(Sprite sprite) {
    this(sprite.getClass().getSimpleName(), sprite.getInputString(), sprite.getCoordinates());
  }

  /**
   * Gets the spriteClassName of this SpriteDescription.
   *
   * @return A String containing the spriteClassName of this SpriteDescription
   */
  @JsonGetter
  public String getClassName() {
    return spriteClassName;
  }

  /**
   * Gets the inputSource String of this SpriteDescription.
   *
   * @return A String containing the inputSource String of this SpriteDescription
   */
  @JsonGetter
  public String getInputSource() {
    return inputSource;
  }

  /**
   * Gets the coordinates of this SpriteDescription.
   *
   * @return SpriteCoordinates containing the current location of this SpriteDescription
   */
  @JsonGetter
  public SpriteCoordinates getCoordinates() {
    return coordinates;
  }

  /**
   * This method converts a SpriteDescription to a new Sprite by leveraging reflection to construct
   * a new Sprite of the correct type from the simple spriteClassName and passing in the
   * SpriteDescription itself into the new Sprite's constructor.
   *
   * @return A properly instantiated Sprite of the type specificed by spriteClassName, and with the
   *     correct inputSource String and coordinates.
   */
  public Sprite toSprite() {
    try {
      Class<?> spriteClass = Class.forName("ooga.model.sprites." + spriteClassName);
      Sprite spriteToReturn =
          (Sprite) spriteClass.getDeclaredConstructor(SpriteDescription.class).newInstance(this);
      spriteToReturn.setInputString(inputSource);
      return spriteToReturn;
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InstantiationException
        | InvocationTargetException
        | IllegalAccessException e) {
    }
    return null;
  }
}
