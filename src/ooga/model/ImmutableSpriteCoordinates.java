package ooga.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ooga.util.Vec2;

public interface ImmutableSpriteCoordinates {
    @JsonGetter
    Vec2 getPosition();

    @JsonIgnore
    TileCoordinates getTileCoordinates();

    /**
     * Returns the coordinates of the center of the tile currently resided by the Sprite associated
     * with these coordinates.
     *
     * @return Vec2 containing the center of the Tile
     */
    @JsonIgnore
    Vec2 getTileCenter();

    @Override
    String toString();
}
