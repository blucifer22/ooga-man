package ooga.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.KeyCode;
import ooga.model.InputSource;
import ooga.util.Vec2;
import ooga.view.io.HumanInputConsumer;

/**
 * HumanInputManager handles the interpretation of the currently pressed keys on the keyboard and
 * translates them to Vec2's that can be utilized by Sprites.
 *
 * @author Marc Chmielewski
 */
public class HumanInputManager implements InputSource, HumanInputConsumer {

  private final Map<String, KeyCode> keybinding;
  private final Set<KeyCode> pressedKeys;

  /**
   * Basic constructor for HumanInputManager.
   *
   * <p>Creates a new HashSet of pressedKeys.
   */
  public HumanInputManager(KeybindingType keybindingType) {
    if (keybindingType.equals(KeybindingType.PLAYER_1)) {
      keybinding =
          Map.of(
              "UP",
              KeyCode.UP,
              "DOWN",
              KeyCode.DOWN,
              "LEFT",
              KeyCode.LEFT,
              "RIGHT",
              KeyCode.RIGHT,
              "ACTION",
              KeyCode.CONTROL);
    } else {
      keybinding =
          Map.of(
              "UP",
              KeyCode.W,
              "DOWN",
              KeyCode.S,
              "LEFT",
              KeyCode.A,
              "RIGHT",
              KeyCode.D,
              "ACTION",
              KeyCode.SPACE);
    }
    pressedKeys = new HashSet<>();
  }

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
    if ((pressedKeys.contains(keybinding.get("UP"))))
      ret = ret.add(new Vec2(0, -1));
    if ((pressedKeys.contains(keybinding.get("DOWN"))))
      ret = ret.add(new Vec2(0, 1));
    if ((pressedKeys.contains(keybinding.get("LEFT"))))
      ret = ret.add(new Vec2(-1, 0));
    if ((pressedKeys.contains(keybinding.get("RIGHT"))))
      ret = ret.add(new Vec2(1, 0));
    return ret;
  }

  /**
   * On receiving a depressed KeyCode from the front-end, add the KeyCode to the Set of pressedKeys.
   *
   * @param code The KeyCode of the currently depressed key.
   */
  @Override
  public void onKeyPress(KeyCode code) {
    System.out.println(code);
    pressedKeys.add(code);
  }

  /**
   * On receiving a released KeyCode from the front-end, remove the KeyCode from the Set of
   * pressedKeys.
   *
   * @param code The KeyCode of the currently released key.
   */
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
    return pressedKeys.contains(keybinding.get("ACTION"));
  }
}
