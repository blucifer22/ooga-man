package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.api.TileEvent;
import ooga.model.api.TileEvent.EventType;
import ooga.model.grid.TileCoordinates;
import org.junit.jupiter.api.Test;

public class TileTests {

  @Test
  public void testTileCoordinates(){
    TileCoordinates tileCoords = new TileCoordinates();
    assertEquals(0, tileCoords.getX());
    assertEquals(0, tileCoords.getY());
  }

  @Test
  public void testTileEvent(){
    TileEvent tileEvent = new TileEvent(EventType.TYPE_CHANGE);
    assertEquals(EventType.TYPE_CHANGE, tileEvent.getEventType());
  }

}
