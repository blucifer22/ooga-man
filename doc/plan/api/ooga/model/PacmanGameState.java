/**
 *
 */

public interface PacmanGameState {
    private PacmanGrid grid;
    private Collection<Sprite> sprites;

    // advance game state by `dt' seconds
    public void step(double dt);
    public Collection<Sprite> getSprites();
    public Collection<Sprite> getMovingSprites();
    public Collection<Sprite> getStationarySprites();

    public PacmanGrid getGrid();
}
