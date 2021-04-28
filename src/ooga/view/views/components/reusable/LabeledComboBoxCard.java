package ooga.view.views.components.reusable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ooga.view.uiservice.UIServiceProvider;

/**
 * A visual card containing a {@link Label} directly above a {@link ComboBox}.
 *
 * @author David Coffman
 */
public class LabeledComboBoxCard extends StackPane {

  /**
   * {@link Map} constructor used when option keys (i.e. what the {@link OptionSelectionHandler}
   * expects) differ from the labels to display to the user.
   *
   * @param serviceProvider a {@link UIServiceProvider} providing UI services as desired
   * @param labelBundleIdentifier the {@link ooga.view.language.api.LanguageService} key for the
   *                              localized label string
   * @param options a {@link Map} of option keys to their labels
   * @param selectionHandler an {@link OptionSelectionHandler} to call when a new option has been
   *                        selected
   */
  public LabeledComboBoxCard(
      UIServiceProvider serviceProvider,
      String labelBundleIdentifier,
      Map<String, String> options,
      OptionSelectionHandler selectionHandler) {
    super();
    configure(serviceProvider, labelBundleIdentifier, options, selectionHandler);
  }

  /**
   * {@link Collection} constructor used when option keys <em>do not</em> differ from the labels
   * to display to the user.
   *
   * @param serviceProvider a {@link UIServiceProvider} providing UI services as desired
   * @param labelBundleIdentifier the {@link ooga.view.language.api.LanguageService} key for the
   *    *                              localized label string
   * @param options a {@link Collection} of options
   * @param selectionHandler an {@link OptionSelectionHandler} to call when a new option has been
   *    *                        selected
   */
  public LabeledComboBoxCard(
      UIServiceProvider serviceProvider,
      String labelBundleIdentifier,
      Collection<String> options,
      OptionSelectionHandler selectionHandler) {
    super();
    LinkedHashMap<String, String> orderedOptions = new LinkedHashMap<>();
    for (String option : options) {
      orderedOptions.put(option, option);
    }
    configure(serviceProvider, labelBundleIdentifier, orderedOptions, selectionHandler);
  }

  // Configures the labeled ComboBox card.
  private void configure(
      UIServiceProvider serviceProvider,
      String labelBundleIdentifier,
      Map<String, String> options,
      OptionSelectionHandler selectionHandler) {
    Label dropdownLabel =
        new StyledBoundLabel(
            serviceProvider.languageService().getLocalizedString(labelBundleIdentifier),
            "dropdown-label",
            labelBundleIdentifier + "-select-label");

    ArrayList<Pair<String, String>> dropdownOptions = new ArrayList<>();
    for (String key : options.keySet()) {
      dropdownOptions.add(
          new Pair<>(key, options.get(key)) {
            @Override
            public String toString() {
              return this.getValue();
            }
          });
    }

    ComboBox<Pair<String, String>> dropdown = new ComboBox<>();
    dropdown.getItems().addAll(dropdownOptions);
    dropdown.getStyleClass().add("dropdown");
    dropdown.setId(labelBundleIdentifier + "-select-dropdown");
    dropdown.setOnAction(e -> selectionHandler.onSelection(dropdown.getValue().getKey()));

    VBox labeledLangDropdown = new VBox(dropdownLabel, dropdown);
    labeledLangDropdown.getStyleClass().add("card");
    labeledLangDropdown.setId(labelBundleIdentifier + "-select-card");

    this.getChildren().add(labeledLangDropdown);
  }

  @FunctionalInterface
  public interface OptionSelectionHandler {

    /**
     * Called when a new option is selected in the ComboBox.
     *
     * @param selectedOption the new option selected in the ComboBox.
     */
    void onSelection(String selectedOption);
  }
}
