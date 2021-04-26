package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.sprites.status.GhostWin;
import ooga.model.sprites.status.PacmanWin;
import ooga.util.Vec2;
import org.junit.jupiter.api.Test;

public class WinTest {

  PacmanGameState pacmanGameState = new PacmanGameState();

  @Test
  public void testGhostWin(){
    GhostWin ghostWin = new GhostWin(new SpriteCoordinates(new Vec2(0,0)), new Vec2(0,0));
    ghostWin.uponHitBy(ghostWin, pacmanGameState);
    assertEquals(new SpriteCoordinates(new Vec2(0,0)).getTileCoordinates().getX(), ghostWin.getCoordinates().getTileCoordinates().getX());
    assertEquals(new SpriteCoordinates(new Vec2(0,0)).getTileCoordinates().getY(), ghostWin.getCoordinates().getTileCoordinates().getY());
  }

  @Test
  public void testPacManWin(){
    PacmanWin pacmanWin = new PacmanWin(new SpriteCoordinates(new Vec2(0,0)), new Vec2(0,0));
    pacmanWin.uponHitBy(pacmanWin, pacmanGameState);
    assertEquals(new SpriteCoordinates(new Vec2(0,0)).getTileCoordinates().getX(), pacmanWin.getCoordinates().getTileCoordinates().getX());
    assertEquals(new SpriteCoordinates(new Vec2(0,0)).getTileCoordinates().getY(), pacmanWin.getCoordinates().getTileCoordinates().getY());
  }

}
