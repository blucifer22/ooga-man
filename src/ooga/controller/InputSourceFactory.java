package ooga.controller;

import java.util.ArrayList;
import java.util.List;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * This class contains methods that handle the attachment of targets to InputSources.
 *
 * @author Marc Chmielewski
 * @author George Hong
 */
public class InputSourceFactory {
  public void attachTargets(List<Sprite> spriteList) {
    List<Ghost> ghostList = new ArrayList<>();
    PacMan pacman = null;
    for (Sprite sprite : spriteList) {
      if (sprite.getSwapClass() == SwapClass.PACMAN) {
        pacman = (PacMan) sprite;
      } else if (sprite.getSwapClass() == SwapClass.GHOST) {
        ghostList.add((Ghost) sprite);
      }
    }
  }
}
