package ooga.view.views;

import java.util.ArrayList;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ooga.view.uiservice.UIServiceProvider;

public class LabeledComboboxCard extends StackPane {
  public LabeledComboboxCard(UIServiceProvider serviceProvider, String labelBundleIdentifier,
      Map<String, String> options, OptionSelectionHandler selectionHandler) {
    super();
    Label dropdownLabel = new Label();
    dropdownLabel.textProperty().bind(serviceProvider.languageService().getLocalizedString(labelBundleIdentifier));
    dropdownLabel.getStyleClass().add("dropdown-label");
    dropdownLabel.setId(labelBundleIdentifier+"-select-label");

    ArrayList<Pair<String, String>> dropdownOptions = new ArrayList<>();
    for (String key : options.keySet()) {
      dropdownOptions.add(new Pair<>(key, options.get(key)) {
        @Override
        public String toString() {
          return this.getValue();
        }
      });
    }

    ComboBox<Pair<String, String>> dropdown = new ComboBox<>();
    dropdown.getItems().addAll(dropdownOptions);
    dropdown.getStyleClass().add("dropdown");
    dropdown.setId(labelBundleIdentifier+"-select-dropdown");
    dropdown.setOnAction(e -> selectionHandler.onSelection(dropdown.getValue().getKey()));

    VBox labeledLangDropdown = new VBox(
        dropdownLabel,
        dropdown
    );
    labeledLangDropdown.getStyleClass().add("card");
    labeledLangDropdown.setId(labelBundleIdentifier+"-select-card");

    this.getChildren().add(labeledLangDropdown);
  }

  @FunctionalInterface
  public interface OptionSelectionHandler {
    void onSelection(String selectedOption);
  }
}
