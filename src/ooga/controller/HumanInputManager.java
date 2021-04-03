package ooga.controller;

import javafx.scene.input.KeyCode;
import ooga.model.InputSource;
import ooga.util.Vec2;
import ooga.view.HumanInputConsumer;

public class HumanInputManager implements InputSource, HumanInputConsumer {

  /**
   * This method queries the source for the _current_ state of which directional inputs are being
   * given to the sprite.
   * <p>
   * If no key is being held down, this should return Vec2.ZERO.
   * <p>
   * The sign convention for this return value is the raster convention -- +Y is directed
   * downwards.
   *
   * @return currently requested input direction, or Vec2.ZERO if none
   */
  @Override
  public Vec2 getRequestedDirection() {
    return null;
  }

  /**
   * Checks if the "action" button is being pressed or otherwise requested.
   * <p>
   * For example, if the user holds down spacebar in the human input manager, this method should
   * return true.
   *
   * @return whether the "action" input is being held or otherwise requested (i.e. by AI input
   * sources)
   */
  @Override
  public boolean isActionPressed() {
    return false;
  }

  @Override
  public void onKeyPress(KeyCode keyCode) {

  }
  //TODO: Translate keypresses to inputs for sprites ex.) keypress ==> vec2

  Set<KeyCode> pressedKeys;

  @Override
  public Vec2 getRequestedDirection() {
    Vec2 ret;
    // in case pressedKeys contains UP, ret += (1, 0)
    // pressedKeys contains DOWN, ret -= (1, 0)

  }

  @Override
  public void onKeyPress(KeyCode code) {
    pressedKeys.add(code);
  }

  @Override
  public void onKeyRelease(KeyCode code) {
    pressedKeys.remove(code);
  }
}
