package ooga.model;

public interface TileObservable {

  TileCoordinates getCoordinates();
  String getType();
  void addTileObserver(TileObserver observer, TileEvent.EventType... events);
}
