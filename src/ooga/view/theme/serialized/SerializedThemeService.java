package ooga.view.theme.serialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeSelectionService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

public class SerializedThemeService implements ThemeService, ThemeSelectionService {

  private static final String USER_THEME_PATH = "data/themes/";
  private static final String THEME_MANIFEST_NAME = "theme.json";
  private static final String DEFAULT_THEME_PATH = "data/themes/classic/theme.json";
  private static final String DEFAULT_THEME_NAME = "Classic";
  private final HashSet<ThemedObject> observers;
  private final HashMap<String, Theme> availableThemes;
  private Theme theme;

  public SerializedThemeService() {
    observers = new HashSet<>();
    availableThemes = new HashMap<>();
    refreshAvailableThemes();
    setTheme(DEFAULT_THEME_NAME);
  }

  @Override
  public Theme getTheme() {
    return this.theme;
  }

  @Override
  public void setTheme(String name) {
    this.theme = availableThemes.get(name);

    for (ThemedObject observer : observers) {
      observer.onThemeChange();
    }
  }

  @Override
  public void addThemedObject(ThemedObject themedObject) {
    this.observers.add(themedObject);
    themedObject.onThemeChange();
  }

  @Override
  public Set<String> getAvailableThemes() {
    return this.availableThemes.keySet();
  }

  @Override
  public void refreshAvailableThemes() {
    this.availableThemes.clear();

    File defaultTheme = new File(DEFAULT_THEME_PATH);
    try {
      loadThemeFromFile(defaultTheme);
    } catch (IOException e) {
      // VERY BAD: means default theme is missing! This should never happen!!
      e.printStackTrace();
    }

    File base = new File(USER_THEME_PATH);
    recursiveDirectoryTraversal(base);
  }

  private void recursiveDirectoryTraversal(File base) {
    if (base.isFile() && base.exists() && base.getName().equals(THEME_MANIFEST_NAME)) {
      try {
        loadThemeFromFile(base);
      } catch (Exception e) {
        // TODO: handle exception
      }
    } else if (base.isDirectory() && base.exists()) {
      for (File f : Objects.requireNonNull(base.listFiles())) {
        recursiveDirectoryTraversal(f);
      }
    }
  }

  private void loadThemeFromFile(File f) throws IOException {
    Theme t = (new ObjectMapper()).readValue(f, ThemeDescription.class).toTheme();
    availableThemes.put(t.getName(), t);
  }
}

