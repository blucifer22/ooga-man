package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.input.KeyCode;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HumanInputManagerTests {

  HumanInputManager player1InputManager;
  HumanInputManager player2InputManager;

  @BeforeEach
  public void setupHumanInputManager() {
//    player1InputManager = new HumanInputManager(KeybindingType.PLAYER_1);
//    player2InputManager = new HumanInputManager(KeybindingType.PLAYER_2);
  }

  @Test
  public void testUpOnKeyPress() {
//    // Add a key press
//    player1InputManager.onKeyPress(KeyCode.UP);
//    Vec2 ret = player1InputManager.getRequestedDirection();
//    assertEquals(ret.getX(), 0);
//    assertEquals(ret.getY(), -1);
  }

  @Test
  public void testMultipleOnKeyPress() {
//    // Add some key presses
//    player1InputManager.onKeyPress(KeyCode.UP);
//    player1InputManager.onKeyPress(KeyCode.DOWN);
//    player1InputManager.onKeyPress(KeyCode.RIGHT);
//    Vec2 ret = player1InputManager.getRequestedDirection();
//    assertEquals(ret.getX(), 1);
//    assertEquals(ret.getY(), 0);
  }

  @Test
  public void testAdditionAndRemoval() {
//    // Add and then remove some key presses
//    player1InputManager.onKeyPress(KeyCode.UP);
//    player1InputManager.onKeyRelease(KeyCode.UP);
//    player1InputManager.onKeyPress(KeyCode.DOWN);
//    Vec2 ret = player1InputManager.getRequestedDirection();
//    assertEquals(ret.getX(), 0);
//    assertEquals(ret.getY(), 1);
  }

  @Test
  public void testPressAndHoldAction() {
//    // Press control, query isActionPressed() a few times, and then release
//    player1InputManager.onKeyPress(KeyCode.CONTROL);
//    assertTrue(player1InputManager.isActionPressed());
//    assertTrue(player1InputManager.isActionPressed());
//    player1InputManager.onKeyRelease(KeyCode.CONTROL);
//    assertFalse(player1InputManager.isActionPressed());
  }

  @Test
  public void testConcurrentMoveAndAction() {
//    // Press control and the arrow keys
//    player1InputManager.onKeyPress(KeyCode.CONTROL);
//    player1InputManager.onKeyPress(KeyCode.RIGHT);
//    Vec2 ret = player1InputManager.getRequestedDirection();
//    assertEquals(ret.getX(), 1);
//    assertTrue(player1InputManager.isActionPressed());
  }

  @Test
  public void testWASDKeybinding() {
//    player2InputManager.onKeyPress(KeyCode.W);
//    assertEquals(player2InputManager.getRequestedDirection().getX(), 0);
//    player2InputManager.onKeyRelease(KeyCode.W);
//    player2InputManager.onKeyPress(KeyCode.A);
//    assertEquals(player2InputManager.getRequestedDirection().getX(), -1);
//    player2InputManager.onKeyRelease(KeyCode.A);
//    player2InputManager.onKeyPress(KeyCode.S);
//    assertEquals(player2InputManager.getRequestedDirection().getX(), 0);
//    player2InputManager.onKeyPress(KeyCode.S);
//    player2InputManager.onKeyPress(KeyCode.D);
//    assertEquals(player2InputManager.getRequestedDirection().getX(), 1);
//    player2InputManager.onKeyRelease(KeyCode.D);
  }
}
