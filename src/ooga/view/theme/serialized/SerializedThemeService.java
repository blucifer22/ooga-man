package ooga.view.theme.serialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeSelectionService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

/**
 * A management class for {@link SerializedTheme}s. Implements {@link ThemeService} and
 * {@link ThemeSelectionService}, meaning that {@link SerializedThemeService} allows both for
 * {@link ThemedObject}s to register for theme-change updates and  query for the
 * current {@link Theme} <em>and</em> .
 */
public class SerializedThemeService implements ThemeService, ThemeSelectionService {

  private static final String USER_THEME_PATH = "data/themes/";
  private static final String THEME_MANIFEST_NAME = "theme.json";
  private static final String DEFAULT_THEME_PATH = "data/themes/classic/theme.json";
  private static final String DEFAULT_THEME_NAME = "Classic";
  private final HashSet<ThemedObject> observers;
  private final HashMap<String, Theme> availableThemes;
  private final ExceptionService exceptionService;
  private Theme theme;

  /**
   * Sole constructor for {@link SerializedThemeService}. Takes a single {@link ExceptionService}
   * as a parameter.
   *
   * @param exceptionService the {@link ExceptionService} to notify if an operation fails
   */
  public SerializedThemeService(ExceptionService exceptionService) {
    this.exceptionService = exceptionService;
    this.observers = new HashSet<>();
    this.availableThemes = new HashMap<>();
    this.refreshAvailableThemes();
    this.setTheme(DEFAULT_THEME_NAME);
  }

  /**
   * Returns the active {@link Theme}.
   *
   * @return the active {@link Theme}.
   */
  @Override
  public Theme getTheme() {
    return this.theme;
  }

  /**
   * Sets a new theme.
   *
   * @param name the name of the {@link Theme} to enable
   */
  @Override
  public void setTheme(String name) {
    this.theme = availableThemes.get(name);

    for (ThemedObject observer : observers) {
      observer.onThemeChange();
    }
  }

  /**
   * Registers a {@link ThemedObject} for theme change updates.
   *
   * @param themedObject the {@link ThemedObject} to register for theme change updates
   */
  @Override
  public void addThemedObject(ThemedObject themedObject) {
    this.observers.add(themedObject);
    themedObject.onThemeChange();
  }

  /**
   * Returns a {@link Set<String>} containing the names of all available {@link Theme}s.
   *
   * @return a {@link Set<String>} containing the names of all available {@link Theme}s.
   */
  @Override
  public Set<String> getAvailableThemes() {
    refreshAvailableThemes();
    return Collections.unmodifiableSet(this.availableThemes.keySet());
  }

  // Refreshes the available themes from disk.
  private void refreshAvailableThemes() {
    this.availableThemes.clear();

    File defaultTheme = new File(DEFAULT_THEME_PATH);
    try {
      loadThemeFromFile(defaultTheme);
    } catch (IOException | IllegalArgumentException e) {
      exceptionService.handleWarning(
          new UIServicedException("defaultThemeMissingError", DEFAULT_THEME_NAME));
    }

    File base = new File(USER_THEME_PATH);
    recursiveDirectoryTraversal(base);
  }

  // Recursively searches the "data/themes" directory for themes.
  private void recursiveDirectoryTraversal(File base) {
    if (base.isFile() && base.exists() && base.getName().equals(THEME_MANIFEST_NAME)) {
      try {
        loadThemeFromFile(base);
      } catch (Exception e) {
        exceptionService.handleWarning(
            new UIServicedException("corruptedThemeError", base.getAbsolutePath()));
      }
    } else if (base.isDirectory() && base.exists()) {
      for (File f : Objects.requireNonNull(base.listFiles())) {
        recursiveDirectoryTraversal(f);
      }
    }
  }

  // Loads a theme from disk. Throws an exception if the theme file is malformed.
  private void loadThemeFromFile(File f) throws IOException {
    Theme t =
        (new ObjectMapper()).readValue(f, ThemeDescription.class).toTheme(this.exceptionService);

    if (t.getName() == null) {
      throw new IllegalArgumentException();
    }

    availableThemes.put(t.getName(), t);
  }
}
