package ooga.view.theme.api;

import java.util.Set;

public interface ThemeSelectionService {

  void setTheme(String name);

  Set<String> getAvailableThemes();

}
