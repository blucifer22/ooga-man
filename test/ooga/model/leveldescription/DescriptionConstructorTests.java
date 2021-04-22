package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.SpriteCoordinates;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.junit.jupiter.api.Test;

public class DescriptionConstructorTests {

  @Test
  public void testSpriteFromSpriteDescription() {
    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);
    SpriteDescription description = new SpriteDescription(pacman);
    Sprite reconstructedSprite = description.toSprite();

    assertEquals(reconstructedSprite.getClass().getSimpleName(), "PacMan");
    assertEquals(reconstructedSprite.getCoordinates().getPosition().getX(), 1.5);
  }

}
