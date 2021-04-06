import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.leveldescription.SpriteLayoutDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteLayoutDescriptionTests {
  SpriteLayoutDescription layoutDescription;

  @BeforeEach
  public void setLayoutDescription() {
    String spriteClassName = "ooga.model.sprites.Ghost";
    String inputSource = "GHOST_AI";
    TileCoordinates startingCoordinates = new TileCoordinates(5, 5);
    SpriteDescription ghostDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    spriteClassName = "ooga.model.sprites.Pacman";
    inputSource = "HUMAN";
    startingCoordinates = new TileCoordinates(10, 10);
    SpriteDescription pacmanDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    layoutDescription = new SpriteLayoutDescription(
        List.of(ghostDescription, pacmanDescription));
  }


  @Test
  public void testSpriteLayoutDescriptionConstructor() {
    assertEquals(layoutDescription.getSprites().get(0).getClassName(), "ooga.model.sprites.Ghost");
    assertEquals(layoutDescription.getSprites().get(0).getInputSource(), "GHOST_AI");
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getX(), 5);
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getY(), 5);

    assertEquals(layoutDescription.getSprites().get(1).getClassName(), "ooga.model.sprites.Pacman");
    assertEquals(layoutDescription.getSprites().get(1).getInputSource(), "HUMAN");
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getX(), 10);
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getY(), 10);
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

    assertEquals(layoutDescription.getSprites().get(0).getClassName(), "ooga.model.sprites.Ghost");
    assertEquals(layoutDescription.getSprites().get(0).getInputSource(), "GHOST_AI");
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getX(), 5);
    assertEquals(layoutDescription.getSprites().get(0).getCoordinates().getY(), 5);

    assertEquals(layoutDescription.getSprites().get(1).getClassName(), "ooga.model.sprites.Pacman");
    assertEquals(layoutDescription.getSprites().get(1).getInputSource(), "HUMAN");
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getX(), 10);
    assertEquals(layoutDescription.getSprites().get(1).getCoordinates().getY(), 10);
  }

}
