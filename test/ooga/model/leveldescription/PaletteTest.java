package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.PacmanGameState;
import ooga.model.PacmanLevel;
import ooga.model.TileCoordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaletteTest {

  private Palette palette;

  @BeforeEach
  public void beforeEach() {
    palette = new Palette();
  }

  @Test
  public void loadKeysTest() {
    List<String> names = palette.getSpriteNames();
    String[] allNames = {
        "Blinky",
        "Inky",
        "Clyde",
        "Pinky",
        "PacMan",
        "Cherry",
        "Dot",
        "PowerPill"
    };
    for (String requiredName : allNames) {
      assertTrue(names.contains(requiredName));
    }
  }

}
