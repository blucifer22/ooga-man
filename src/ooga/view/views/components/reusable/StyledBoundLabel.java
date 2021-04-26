package ooga.view.views.components.reusable;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.Label;

public class StyledBoundLabel extends Label {

  public StyledBoundLabel(StringBinding labelBinding, String labelClass) {
    this.textProperty().bind(labelBinding);
    style(labelClass);
  }

  public StyledBoundLabel(StringBinding labelBinding, String labelClass, String id) {
    this(labelBinding, labelClass);
    this.setId("label-"+id);
  }

  public StyledBoundLabel(ReadOnlyStringProperty labelProperty, String labelClass) {
    this.textProperty().bind(labelProperty);
    style(labelClass);
  }

  public StyledBoundLabel(ReadOnlyStringProperty labelProperty, String labelClass, String id) {
    this(labelProperty, labelClass);
    this.setId("label-"+id);
  }

  private void style(String labelClass) {
    this.getStyleClass().addAll("styled-label", "styled-label-" + labelClass);
  }

  public StyledBoundLabel wrap(boolean wrap) {
    this.setWrapText(wrap);
    return this;
  }
}
