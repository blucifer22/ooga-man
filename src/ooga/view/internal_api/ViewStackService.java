package ooga.view.internal_api;

/**
 * UI service responsible for managing the view stack. When a view is done (such as when a
 * "return to main menu" button is pressed), the view must call the
 * {@link ViewStackService#unwind()} method on (most likely) the {@link ViewStackService}
 * retrieved from its {@link ooga.view.uiservice.UIServiceProvider}.
 *
 * @author David Coffman
 */
public interface ViewStackService {

  /**
   * Callback for when a view is done (such as when a "return to main menu" button is pressed).
   * The view must call the {@link ViewStackService#unwind()} method on (most likely) the
   * {@link ViewStackService} retrieved from its {@link ooga.view.uiservice.UIServiceProvider} in
   * order to return to the previous view.
   */
  void unwind();
}
