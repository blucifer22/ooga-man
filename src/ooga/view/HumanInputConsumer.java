package ooga.view;

import javafx.scene.input.KeyCode;

/**
 * Front-end interface to handle human inputs
 */
public interface HumanInputConsumer {
  void onKeypress(KeyCode keyCode);
}
