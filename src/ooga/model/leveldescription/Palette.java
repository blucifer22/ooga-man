package ooga.model.leveldescription;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import ooga.model.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class Palette {

  private final ResourceBundle bundle;
  private final List<String> spriteOptions;
  private String activeSprite;

  public Palette() {
    bundle = ResourceBundle.getBundle("resources.util.spriteinputs");
    spriteOptions = new ArrayList<>();
    for (String name : bundle.keySet()) {
      spriteOptions.add(name);
    }
    setActiveSprite(spriteOptions.get(0));
  }

  /**
   * Returns a list of all Sprites that can be converted into SpriteDescriptions.
   *
   * @return
   */
  public List<String> getSpriteNames() {
    return new ArrayList<>(spriteOptions);
  }

  /**
   * Sprite to set as the active Sprite that will be "painted" onto the grid.
   *
   * @param activeSprite
   */
  public void setActiveSprite(String activeSprite) {
    if (!spriteOptions.contains(activeSprite)) {
      throw new IllegalArgumentException("Active sprite choice is not a valid option");
    }
    this.activeSprite = activeSprite;
  }

  /**
   * @param x x-coordinate of tile to place Sprite
   * @param y y-coordinate
   * @return
   */
  public Sprite getSprite(double x, double y) {
    String inputSource = bundle.getString(activeSprite);
    SpriteCoordinates coordinates = new SpriteCoordinates(new Vec2(x, y));
    SpriteDescription description = new SpriteDescription(activeSprite, inputSource, coordinates);
    return description.toSprite();
  }
}
