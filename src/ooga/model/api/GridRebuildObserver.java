package ooga.model.api;

/**
 * Interface to be implemented by objects which should
 * be notified of a grid rebuild.
 */
public interface GridRebuildObserver {

  /**
   * Called upon grid rebuild.
   * @param grid Grid which is being rebuilt.
   */
  void onGridRebuild(ObservableGrid grid);
}
