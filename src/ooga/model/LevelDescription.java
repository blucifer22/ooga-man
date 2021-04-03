package ooga.model;

import java.util.Collection;

/**
 * LevelDescription contains all of the information required to construct an ObservableGrid
 * (AKA: A Pac-Man level) including width, height, and Sprites.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class LevelDescription {
    private int width, height;
    private Tile grid[][];
    private Collection<Sprite> sprites;
}