package ooga.model.api;

/**
 * A observer interface for notification of grid rebuilds.
 *
 * @author David Coffman
 */
public interface GridRebuildObservable {
  /**
   * Add an observer.
   * @param observer Observer.
   */
  void addGridRebuildObserver(GridRebuildObserver observer);
}
