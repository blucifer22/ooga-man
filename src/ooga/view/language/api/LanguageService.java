package ooga.view.language.api;

import javafx.beans.property.ReadOnlyStringProperty;

public interface LanguageService {
  ReadOnlyStringProperty getLocalizedString(String s);
}
