import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.input.KeyCode;
import ooga.controller.HumanInputManager;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HumanInputManagerTests {

  HumanInputManager inputManager;

  @BeforeEach
  public void setupHumanInputManager() {
    inputManager = new HumanInputManager();
  }

  @Test
  public void testUpOnKeyPress() {
    // Add a key press
    inputManager.onKeyPress(KeyCode.UP);
    Vec2 ret = inputManager.getRequestedDirection();
    assertEquals(ret.getX(), 0);
    assertEquals(ret.getY(), -1);
    ;
  }

  @Test
  public void testMultipleOnKeyPress() {
    // Add some key presses
    inputManager.onKeyPress(KeyCode.UP);
    inputManager.onKeyPress(KeyCode.DOWN);
    inputManager.onKeyPress(KeyCode.RIGHT);
    Vec2 ret = inputManager.getRequestedDirection();
    assertEquals(ret.getX(), 1);
    assertEquals(ret.getY(), 0);
  }

  @Test
  public void testAdditionAndRemoval() {
    // Add and then remove some key presses
    inputManager.onKeyPress(KeyCode.UP);
    inputManager.onKeyRelease(KeyCode.UP);
    inputManager.onKeyPress(KeyCode.DOWN);
    Vec2 ret = inputManager.getRequestedDirection();
    assertEquals(ret.getX(), 0);
    assertEquals(ret.getY(), 1);
  }

  @Test
  public void testPressAndHoldAction() {
    // Press space, query isActionPressed() a few times, and then release
    inputManager.onKeyPress(KeyCode.SPACE);
    assertTrue(inputManager.isActionPressed());
    assertTrue(inputManager.isActionPressed());
    inputManager.onKeyRelease(KeyCode.SPACE);
    assertFalse(inputManager.isActionPressed());
  }

  @Test
  public void testConcurrentMoveAndAction() {
    // Press space and the arrow keys
    inputManager.onKeyPress(KeyCode.SPACE);
    inputManager.onKeyPress(KeyCode.RIGHT);
    Vec2 ret = inputManager.getRequestedDirection();
    assertEquals(ret.getX(), 1);
    assertTrue(inputManager.isActionPressed());
  }
}
