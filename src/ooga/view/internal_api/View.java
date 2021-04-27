package ooga.view.internal_api;

import javafx.scene.layout.Pane;

public interface View extends Renderable {

  @Override
  Pane getRenderingNode();
}
