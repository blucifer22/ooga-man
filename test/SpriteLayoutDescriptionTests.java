import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.leveldescription.SpriteLayoutDescription;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteLayoutDescriptionTests {
  SpriteLayoutDescription layoutDescription;

  @BeforeEach
  public void setLayoutDescription() {
    String spriteClassName = "Ghost";
    String inputSource = "GHOST_AI";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    SpriteDescription ghostDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    spriteClassName = "PacMan";
    inputSource = "HUMAN";
    startingCoordinates = new SpriteCoordinates(new Vec2(10, 10));
    SpriteDescription pacmanDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    layoutDescription = new SpriteLayoutDescription(
        List.of(ghostDescription, pacmanDescription));
  }


  @Test
  public void testSpriteLayoutDescriptionConstructor() {
    assertEquals(layoutDescription.getSprites().get(0).getClassName(), "Ghost");
    assertEquals(layoutDescription.getSprites().get(0).getInputSource(), "GHOST_AI");
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getPosition().getX(), 5);
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getPosition().getY(), 5);

    assertEquals(layoutDescription.getSprites().get(1).getClassName(), "PacMan");
    assertEquals(layoutDescription.getSprites().get(1).getInputSource(), "HUMAN");
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getPosition().getX(), 10);
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getPosition().getY(), 10);
  }

  @Test
  public void testSpriteLayoutDescriptionJSON() {
    String path = "data/levels/sprite-layouts/test_sprite_layout.json";


    try {
      layoutDescription.toJSON(path);
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    JSONDescriptionFactory jsonDescriptionFactory = new JSONDescriptionFactory();
    try{
      layoutDescription = jsonDescriptionFactory.getSpriteLayoutDescriptionFromJSON(path);
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(layoutDescription.getSprites().get(0).getClassName(), "Ghost");
    assertEquals(layoutDescription.getSprites().get(0).getInputSource(), "GHOST_AI");
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getPosition().getX(), 5);
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getPosition().getY(), 5);

    assertEquals(layoutDescription.getSprites().get(1).getClassName(), "PacMan");
    assertEquals(layoutDescription.getSprites().get(1).getInputSource(), "HUMAN");
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getPosition().getX(), 10);
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getPosition().getY(), 10);
  }

}
