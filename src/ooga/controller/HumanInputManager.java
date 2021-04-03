package ooga.controller;

import java.util.Set;
import javafx.scene.input.KeyCode;
import ooga.model.InputSource;
import ooga.util.Vec2;
import ooga.view.HumanInputConsumer;

public class HumanInputManager implements InputSource, HumanInputConsumer {

  Set<KeyCode> pressedKeys;

  public HumanInputManager() {}

  /**
   * This method queries the source for the _current_ state of which directional inputs are being
   * given to the sprite.
   *
   * <p>If no key is being held down, this should return Vec2.ZERO.
   *
   * <p>The sign convention for this return value is the raster convention -- +Y is directed
   * downwards.
   *
   * @return currently requested input direction, or Vec2.ZERO if none
   */
  @Override
  public Vec2 getRequestedDirection() {
    Vec2 ret = Vec2.ZERO;
    if (pressedKeys.contains(KeyCode.UP)) ret.add(new Vec2(0, -1));
    if (pressedKeys.contains(KeyCode.DOWN)) ret.add(new Vec2(0, 1));
    if (pressedKeys.contains(KeyCode.LEFT)) ret.add(new Vec2(-1, 0));
    if (pressedKeys.contains(KeyCode.RIGHT)) ret.add(new Vec2(1, 0));
    return ret;
  }

  @Override
  public void onKeyPress(KeyCode code) {
    pressedKeys.add(code);
  }

  @Override
  public void onKeyRelease(KeyCode code) {
    pressedKeys.remove(code);
  }

  /**
   * Checks if the "action" button is being pressed or otherwise requested.
   *
   * <p>For example, if the user holds down spacebar in the human input manager, this method should
   * return true.
   *
   * @return whether the "action" input is being held or otherwise requested (i.e. by AI input
   *     sources)
   */
  @Override
  public boolean isActionPressed() {
    return pressedKeys.contains(KeyCode.SPACE);
  }
}
