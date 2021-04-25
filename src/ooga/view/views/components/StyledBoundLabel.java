package ooga.view.views.components;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.Label;

public class StyledBoundLabel extends Label {

  public StyledBoundLabel(StringBinding labelBinding, String labelClass) {
    this.textProperty().bind(labelBinding);
    style(labelClass);
  }

  public StyledBoundLabel(ReadOnlyStringProperty labelProperty, String labelClass) {
    this.textProperty().bind(labelProperty);
    style(labelClass);
  }

  private void style(String labelClass) {
    this.getStyleClass().addAll("styled-label", "styled-label-" + labelClass);
  }
}
