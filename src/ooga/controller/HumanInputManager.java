package ooga.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.KeyCode;
import ooga.model.api.InputSource;
import ooga.model.sprites.Sprite;
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
  private final Set<KeyCode> releasedKeys;

  /**
   * Basic constructor for HumanInputManager.
   *
   * <p>Creates a new HashSet of pressedKeys.
   */
  @JsonCreator
  public HumanInputManager(@JsonProperty("keybindingType") KeybindingType keybindingType) {
    if (keybindingType.equals(KeybindingType.PLAYER_1)) {
      keybinding =
          Map.of(
              "UP", KeyCode.W, "DOWN", KeyCode.S, "LEFT", KeyCode.A, "RIGHT", KeyCode.D, "ACTION",
              KeyCode.Q);
    } else {
      keybinding =
          Map.of(
              "UP", KeyCode.I, "DOWN", KeyCode.K, "LEFT", KeyCode.J, "RIGHT", KeyCode.L, "ACTION",
              KeyCode.U);
    }
    pressedKeys = new HashSet<>();
    releasedKeys = new HashSet<>();
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
   * @param dt
   */
  @Override
  public Vec2 getRequestedDirection(double dt) {
    Vec2 ret = Vec2.ZERO;
    if ((pressedKeys.contains(keybinding.get("UP")))) ret = ret.add(new Vec2(0, -1));
    if ((pressedKeys.contains(keybinding.get("DOWN")))) ret = ret.add(new Vec2(0, 1));
    if ((pressedKeys.contains(keybinding.get("LEFT")))) ret = ret.add(new Vec2(-1, 0));
    if ((pressedKeys.contains(keybinding.get("RIGHT")))) ret = ret.add(new Vec2(1, 0));
    return ret;
  }

  /**
   * On receiving a depressed KeyCode from the front-end, add the KeyCode to the Set of pressedKeys.
   *
   * @param code The KeyCode of the currently depressed key.
   */
  @Override
  public void onKeyPress(KeyCode code) {
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
    releasedKeys.add(code);
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
    boolean pressed = releasedKeys.contains(keybinding.get("ACTION"));
    if (pressed) releasedKeys.remove(keybinding.get("ACTION"));

    return pressed;
  }

  /**
   * Adds a Sprite target to the InputSource.
   *
   * @param target The Sprite to add to the InputSource.
   */
  @Override
  public void addTarget(Sprite target) {
    // Do nothing
  }

  /**
   * Returns whether or not this InputSource is human controlled.
   *
   * @return Whether or not this InputSource is human controlled.
   */
  @Override
  public boolean isHumanControlled() {
    return true;
  }
}
