package ooga.model.leveldescription;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Palette is a class that maintains a list of Sprites to select from and a currently selected
 * Sprite. The most common use-case for this is the LevelBuilder, though conceivably, this could be
 * utilized for other applications as well. The list of spriteOptions to be selected from is loaded
 * in from the spriteinputs resource bundle, which is stored as a .properties file in the resources
 * package.
 *
 * @author George Hong
 */
public class Palette {

  private final ResourceBundle bundle;
  private final List<String> spriteOptions;
  private String activeSprite;

  /**
   * This is the general-purpose constructor for Palette, which loads in the potential Sprites from
   * the spriteinputs resource bundle and instantiates the active Sprite to the first among them in
   * the keyset.
   */
  public Palette() {
    bundle = ResourceBundle.getBundle("resources.util.spriteinputs");
    spriteOptions = new ArrayList<>();
    spriteOptions.addAll(bundle.keySet());
    setActiveSprite(spriteOptions.get(0));
  }

  /**
   * Returns a list of all Sprites that can be converted into SpriteDescriptions.
   *
   * @return A List of Strings of all of the Sprites that can be converted into SpriteDescriptions
   */
  public List<String> getSpriteNames() {
    return new ArrayList<>(spriteOptions);
  }

  /**
   * Sprite to set as the active Sprite that will be "painted" onto the grid. Throws an
   * IllegalArgumentException in the event that an invalid Sprite is selected.
   *
   * @param activeSprite A String that contains the name of the Sprite to set as the active Sprite.
   */
  public void setActiveSprite(String activeSprite) {
    if (!spriteOptions.contains(activeSprite)) {
      throw new IllegalArgumentException("Active sprite choice is not a valid option");
    }
    this.activeSprite = activeSprite;
  }

  /**
   * This method returns an instance of the type of Sprite that is currently set as active within
   * the Palette at the location specified by the passed in X and Y location parameters. This is
   * accomplished by constructing a SpriteDescription from this information and then calling the
   * SpriteDescriptions toSprite() method and returning the result.
   *
   * @param x x-coordinate of the grid Tile to place Sprite
   * @param y y-coordinate of the grid Tile to place the Sprite
   * @return A freshly-instantiated Sprite of this Palette's currently active type at the specified
   *     coordinates.
   */
  public Sprite getSprite(double x, double y) {
    String inputSource = bundle.getString(activeSprite);
    SpriteCoordinates coordinates = new SpriteCoordinates(new Vec2(x, y));
    SpriteDescription description = new SpriteDescription(activeSprite, inputSource, coordinates);
    return description.toSprite();
  }
}
