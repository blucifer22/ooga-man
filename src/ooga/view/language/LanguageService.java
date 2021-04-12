package ooga.view.language;

import javafx.beans.property.ReadOnlyStringProperty;

public interface LanguageService {
  ReadOnlyStringProperty getLocalizedString(String s);
}
