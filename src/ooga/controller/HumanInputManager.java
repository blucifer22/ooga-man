package ooga.controller;

import ooga.model.InputSource;
import ooga.view.HumanInputConsumer;

public class HumanInputManager implements InputSource, HumanInputConsumer {
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
