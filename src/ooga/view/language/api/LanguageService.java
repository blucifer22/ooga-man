package ooga.view.language.api;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * View-facing interface for a localization support class. Uses {@link ReadOnlyStringProperty}
 * object to store its translations; for example, a {@link javafx.scene.control.Label} could set its
 * label according to a certain localized String with resource bundle key "TEST", instead of
 * accessing a resource bundle directly, it would do the following:
 *
 * <code>
 * LanguageService ls = ...; Label myTestLabel = new Label(); myTestLabel.textProperty().bind(ls.getLocalizedString("test"));
 * </code>
 * <p>
 * The {@link javafx.scene.control.Label}'s text will then change automatically when (if) the view
 * language changes.
 */
public interface LanguageService {

  /**
   * Returns a {@link ReadOnlyStringProperty} whose value is bound to the current language's
   * translation for the key parameter. The {@link ReadOnlyStringProperty} is updated whenever the
   * application language changes.s
   *
   * @param s the key to search for
   * @return a {@link ReadOnlyStringProperty} whose value is bound to the current language's
   * translation for the key parameter, or a {@link ReadOnlyStringProperty} if no such key exists.
   */
  ReadOnlyStringProperty getLocalizedString(String s);
}
