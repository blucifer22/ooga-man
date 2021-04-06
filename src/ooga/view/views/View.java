package ooga.view.views;

import javafx.scene.layout.Pane;

public interface View extends Renderable {
  Pane getRenderingNode();
}
