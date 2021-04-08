package ooga.view.theme;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashSet;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.theme.serialized.ThemeDescription;

public class ConcreteThemeService implements ThemeService {

  private static final String THEME_PATH = "data/themes/";
  private static final String THEME_MANIFEST_NAME = "/theme.json";
  private final HashSet<ThemedObject> observers;
  private Theme theme;

  public ConcreteThemeService() {
    observers = new HashSet<>();
    setTheme("classic");
  }

  @Override
  public Theme getTheme() {
    return this.theme;
  }

  public void setTheme(String name) {
    try {
      this.theme =
          (new ObjectMapper()).readValue(new File(THEME_PATH + name + THEME_MANIFEST_NAME),
              ThemeDescription.class).toTheme();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    for (ThemedObject observer : observers) {
      observer.onThemeChange();
    }
  }

  @Override
  public void addThemedObject(ThemedObject themedObject) {
    this.observers.add(themedObject);
  }
}

