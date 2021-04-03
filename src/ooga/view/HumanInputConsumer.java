package ooga.view;

import javafx.scene.input.KeyCode;

/**
 * Interface for classes that consume input events from the frontend.
 *
 * For example, when the user presses an arrow key, this interface's
 * onKeyPress method gets called with the appropriate JavaFX keycode.
 */
public interface HumanInputConsumer {
  void onKeyPress(KeyCode keyCode);
  void onKeyRelease(KeyCode keyCode);
}
