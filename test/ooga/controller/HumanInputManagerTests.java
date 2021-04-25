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
    player1InputManager = new HumanInputManager(KeybindingType.PLAYER_1);
    player2InputManager = new HumanInputManager(KeybindingType.PLAYER_2);
  }

  @Test
  public void testUpOnKeyPress() {
    // Add a key press
    player1InputManager.onKeyPress(KeyCode.W);
    Vec2 ret = player1InputManager.getRequestedDirection(0.0);
    assertEquals(ret.getX(), 0);
    assertEquals(ret.getY(), -1);
  }

  @Test
  public void testMultipleOnKeyPress() {
    // Add some key presses
    player1InputManager.onKeyPress(KeyCode.W);
    player1InputManager.onKeyPress(KeyCode.S);
    player1InputManager.onKeyPress(KeyCode.D);
    Vec2 ret = player1InputManager.getRequestedDirection(0.0);
    assertEquals(ret.getX(), 1);
    assertEquals(ret.getY(), 0);
  }

  @Test
  public void testAdditionAndRemoval() {
    // Add and then remove some key presses
    player1InputManager.onKeyPress(KeyCode.W);
    player1InputManager.onKeyRelease(KeyCode.W);
    player1InputManager.onKeyPress(KeyCode.S);
    Vec2 ret = player1InputManager.getRequestedDirection(0.0);
    assertEquals(ret.getX(), 0);
    assertEquals(ret.getY(), 1);
  }

  @Test
  public void testPressAndHoldAction() {
    // Press control, query isActionPressed() a few times, and then release
    player1InputManager.onKeyPress(KeyCode.X);
    assertTrue(player1InputManager.isActionPressed());
    assertTrue(player1InputManager.isActionPressed());
    player1InputManager.onKeyRelease(KeyCode.X);
    assertFalse(player1InputManager.isActionPressed());
  }

  @Test
  public void testConcurrentMoveAndAction() {
    // Press control and the arrow keys
    player1InputManager.onKeyPress(KeyCode.X);
    player1InputManager.onKeyPress(KeyCode.D);
    Vec2 ret = player1InputManager.getRequestedDirection(0.0);
    assertEquals(ret.getX(), 1);
    assertTrue(player1InputManager.isActionPressed());
  }

  @Test
  public void testIJKLKeybinding() {
    player2InputManager.onKeyPress(KeyCode.I);
    assertEquals(player2InputManager.getRequestedDirection(0.0).getX(), 0);
    player2InputManager.onKeyRelease(KeyCode.I);
    player2InputManager.onKeyPress(KeyCode.J);
    assertEquals(player2InputManager.getRequestedDirection(0.0).getX(), -1);
    player2InputManager.onKeyRelease(KeyCode.J);
    player2InputManager.onKeyPress(KeyCode.K);
    assertEquals(player2InputManager.getRequestedDirection(0.0).getX(), 0);
    player2InputManager.onKeyPress(KeyCode.K);
    player2InputManager.onKeyPress(KeyCode.L);
    assertEquals(player2InputManager.getRequestedDirection(0.0).getX(), 1);
    player2InputManager.onKeyRelease(KeyCode.L);
  }
}
