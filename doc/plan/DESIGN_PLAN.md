# OOGA Design Plan

### Team Number 4 - Lettuce Leaf

### Names
Franklin Wei (fw67)

David Coffman (djc70)

George Hong (grh17)

Marc Chmielewski (msc68)

Matthew Belissary (mab185)

## Introduction

Our game will be an extensible JavaFX implementation of Pac-Man. In addition to recreating the original Pac-Man, we plan to build extensions that allow our program to support variations on the game, including multiplayer, different tile sets, adversarial play (player controls the ghosts), and networked play. Our architecture will be designed to make the game as extensible as possible, with data-driven configurations determining the behavior and appearance of the game.

## Overview

Our codebase will be cleanly segregated into the front-end views and the back-end model. The views will exist in the package ooga.view, and the model will exist in the package ooga.model. There will be an ooga.Controller class to serve as the middleware between the front-end and back-end as well as an ooga.Main class to serve as the entry point to our program.

### Division of Responsibility

The frontend displays the things the user sees directly. It is closely tied to JavaFX. The responsibilities of the frontend are:

1. Display an initial splash screen and menu hierarchy (i.e. displaying things that aren't strictly gameplay-related).
2. Communicating with the controller to establish a new game state with a certain class of backend. (For the first iteration, there will only be one type of backend -- the original single-player Pacman game).
3. Displaying the game state on the screen.
4. Notifying the controller of user input during a game.
5. Displaying error messages to the user upon a backend error.

The backend's overall role is representing the game state and its evolution through time. The backend is completely isolated from platform specifics.

The specific responsibilities of the backend are:

1. Tracking the state of an in-progress game. This includes:
   i. Tracking the state of the background grid, which is a rectangular array of tiles. The locations of tiles are fixed throughout a game, but the contents of each tile location may change.
   ii. Tracking the state of on-screen sprites (things that are overlayed on top of the grid). Sprites can be moving or non-moving.
2. Providing functionality for the frontend to observe and query the state of the in-progress game. This includes:
   i. Observer interfaces for critical points in the life cycle of sprites: creation, and deletion.
   ii. Observer interfaces to track updates to Sprites.
   iii. Observer interfaces to track changes to the grid. Observers apply to the whole grid, not individual tiles.
3.

The controller serves as a small piece of middleware that exists to coordinate the

### Frontend-Backend Interface

A critical rule of thumb for the frontend/backend interface is that the backend should _never_ directly reference the controller or frontend. It can, however, contain indirect references to observer classes that can also be members of the frontend -- but the backend should not be aware that they are members of the frontend. All backend-controller-frontend interaction should happen in the following ways:

1. The backend may provide observer interfaces to objects that the frontend must observe. In this way, the backend can indirectly call a frontend method to notify it of events that occur in the backend. For example, the backend can offer a way for outside code to register "sprite creation" callbacks that will be invoked whenever a sprite is created.

2. The frontend may directly invoke methods in the controller. This will be used for events

3. The controller may directly invoke methods in the backend.

4. The controller may directly invoke methods in the frontend.

Interactions not falling into one of these categories are by default _not_ allowed and should be discussed with the team if they are deemed to be absolutely necessary. In most cases, it is anticipated that such interactions may be better implemented within one of the allowed categories.

### Back-End API

#### class PacmanGameState:

```java
package ooga.model;

import java.util.Collection;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 */
public class PacmanGameState {

  private PacmanGrid grid;
  private Collection<Sprite> sprites;
  private int pacManScore;

  public void addExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {

  }

  // advance game state by `dt' seconds
  public void step(double dt) {

  }

  public Collection<Sprite> getSprites() {
    return null;
  }

  public Collection<Sprite> getMovingSprites() {
    return null;
  }

  public Collection<Sprite> getStationarySprites() {
    return null;
  }

  public PacmanGrid getGrid() {
    return null;
  }

  public void advanceLevel() {
  }
}
```

This class contains all the state of a in-progress pacman game and serves as the top-level class in the model.

A "grid" is a fixed background object. "Sprites" are things that exist on top of the grid, other than pure UI components like the score label, etc. Sprites can be moving or stationary.

A grid consists of a regular arrangement of Tiles. Tiles each have a TileCoordinates object that identifies its position within a grid.

##### Grid Model

The following classes define the structure of the grid.

```java=
package ooga.model;

import java.util.Iterator;

/**
 * Object that represents the structure of the Grid and its contents, along with dimensional
 * properties.
 */
abstract class PacmanGrid implements Iterable<Tile> {

  public int getWidth() {
    return 0;
  }

  public int getHeight() {
    return 0;
  }

  public Tile getTile(TileCoordinates tileCoordinates) {
    return null;
  }

  @Override
  public Iterator<Tile> iterator() {
    return null;
  }
}
```

```java=
package ooga.model;

/**
 * Represents basic properties of a Tile such as whether they can be occupied
 */
public class Tile {

  public TileCoordinates getCoordinates() {
    return null;
  }

  public boolean isOpen() {
    // true if pacman/ghosts can move into this tile
    return false;
  }

  //public String
}
```

```java=
// 2D vector class
class Vec2 {

}
```

```java=
package ooga.model;

/**
 * Represents a coordinate grid location composed of solely integer values
 */
class TileCoordinates {

  public int getX() {
    return 0;
  }

  public int getY() {
    return 0;
  }
}
```

##### Sprite Model

Sprites are things that exist on top of the grid, but are not pure UI elements such as score labels.

The following classes define the possible types of Sprites that can exist within a PacmanGameState.

The primary distinction to be made is whether Sprites are stationary or mobile. Mobile sprites can be in motion (although this does not need to _always_ be true -- mobile sprites can occasionally remain stationary, but the converse is not true -- stationary sprites can never move).

```java=
package ooga.model;

import ooga.controller.SpriteObserver;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 */
public abstract class Sprite implements SpriteObservable {

  abstract boolean isStationary();

  public abstract String getType();

  // coordinates of the tile above which this spirte's center lies
  public SpriteCoordinates getCoordinates() {
    return null;
  }

  public double getOrientation() {
    return 0;
  }

  public boolean isVisible() {
    return false;
  }

  // Observation
  public void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents) {

  }

  public void removeObserver(SpriteObserver so) {

  }

  // advance state by dt seconds
  public abstract void step(double dt);

  public abstract boolean mustBeConsumed();
}
```

```java=
package ooga.model;

public class SpriteCoordinates {

  public TileCoordinates getTileCoordinates() {
    return null;
  }

  public Vec2 getTileOffset() {
    return null;
  }
}
````

```java=
package ooga.model;

/**
 * Stationary sprites can never move.
 */
public abstract class StationarySprite {

}
```

```java=
package ooga.model;

/**
 * Mobile sprites can be in motion (although this does not need to always be true -- mobile sprites
 * can occasionally remain stationary)
 */
public abstract class MovingSprite {

  protected InputSource getInputSource() {
    return null;
  }

  public void setInputSource(InputSource s) {

  }
}
```

```java=
package ooga.model;

public abstract interface InputSource {
  Vec2 getMovementDirection();
  enum ActionInput {
    FIRE,
    SPEED_UP,
    SLOW_DOWN,
    RANDOM_TELEPORT
  }
}
```

**Ghost AI Strategy**
As per *The Pac-Man Dossier*, ghosts have three mutually-exclusive modes of behavior that they may be in during play: chase, scatter, and frightened.

```java=
public abstract class GhostBehavior {
    // As a ghost approaches intersection, it may apply a strategy to chase after Pac-Man, based on its location.  Accepts the ghost's current info.
    public Direction prepareIntersection();
    // tracks reference to Pac-Man (or some other moving object) to help the movement decisions.  Ghosts can also track such targets such as 4 spaces ahead of Pac-man or just a specific block in the maze.
    public void addTarget(Sprite target);
    // Might be helpful for multiple targets
    public void removeTarget(Sprite target);
}

public class ChaseBlinkyBehavior extends GhostBehavior;
// Target tile is a spot off the Grid
public class ScatterBehavior extends GhostBehavior; 
public class FrightenedBehavior extends GhostBehavior;

```
Ghosts and PacMan maintain a direction.  Important for ghosts because they cannot reverse while moving down a hallway (per standard rules).
```java=
class Direction {
    
}
```

**Observation Interfaces**

The frontend will operate largely using observation APIs implemented by the backend.

```java=
public interface SpriteObservable {
    String getType();
    SpriteCoordinates getCenter();
    double getOrientation();
    boolean isVisible();
    void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents);
    void removeObserver(SpriteObserver so);
}
```

```java=
public class SpriteEvent {
    public enum EventType {
        TYPE_CHANGE, SHOW, HIDE, TRANSLATE, ROTATE, SCALE
    }

    public SpriteObservationEvent(SpriteObservable sender, EventType type);

    public SpriteObservable getSender();

    public EventType getEventType();
}
```

```java=
public interface SpriteObserver {
    void onSpriteUpdate(SpriteEvent e);
}
```

```java=
public interface SpriteExistenceObserver {
    void onSpriteCreation(SpriteObservable so);
    void onSpriteDestruction(SpriteObservable so);
}
```

We anticipate being able to reuse these APIs for several potential extensions (including an online multiplayer Pac-Man, where our normal backend would *not* be driving the frontend).


### Controller API
```java=
public class Controller {
    public void startGame() {
        PacmanGameState pgs = new PacmanGameState();
        GameView gv = new GameView();
        pgs.addObserver(gv);
        // start game through a call to PacmanGameState!
    }
}
```

### Frontend API

The frontend's public API consists almost entirely of interface implementations:

```java=
public class GameView implements SpriteExistenceObserver  {
    private Map<SpriteObservable, SpriteView> views;

    public void onSpriteCreation(SpriteObservable so) {
        SpriteView sv = new SpriteView(so);
        views.put(so, sv);
        // render the SpriteView
    }
    
    public void onSpriteDestruction(SpriteObservable so) {
        views.remove(so);
        // de-render the SpriteView
    }
}
```

```java=
public class SpriteView implements SpriteObserver {
    public SpriteView(SpriteObservable so) {
        so.addObserver(this);
        // create the view
    }
    
    public void onSpriteUpdate(SpriteEvent e) {
        // handle the SpriteEvent
    }
}
```


## Design Details
#### Ghost Release
Depending on how much functionality we want to implement, Pac-Man Ghosts mostly begin in their enclosure before leaving:
- `Counter` object of some sort that can track the dot limits for ghosts + real time counter optional.

Ghost Sprite Movement:
#### Ghost Behavior
1. Chase + variants: Often requires Pac-Man's location or some reference to Pacman's location.
2. *Removable* Scatter: heads to home corners, and circles for a while
3. Frightened mode: vulnerable, aimlessly wander
   Internal to Ghosts:
- Store movement direction, be sensitive to state change.


## Design Considerations

* Front-End/Asset Reuse
    * We have a team that is pretty heavily biased towards back-end devs, and as such, it makes sense to select games/genres that are able to reuse a majority of assets and front-end components.
        * One way to approach this is to adopt a data-driven approach to design.
            * This has been successfully demonstrated by games such as *Total Annihilation* which was able to support new troop types in the early internet era by relying almost exclusively on configuration files to implement rules and other game-play characteristics.

* Interface Segregation/Dependency Inversion
    * In pursuit of the previously established goal of extensive code reuse, we're trying to maximize the modularity of the code that we write.
        * As such, we're looking to capitalize on notions of *Interface Segregation* and *Dependency Inversion* to create interchangeable gameplay elements.
        * This will facilitate the rapid iteration of designs, improve the quality of our data-driven approach, and make developing the level-editor ***MUCH*** easier.

## Example Games

* "Traditional" Pac-Man
    * This game is the classic Pac-Man that we all know and love.
    * Features:
        * One maze, one Pac-Man, 4 ghosts, the classic power-ups, you know the deal :smile:
    * Design Considerations:
        * Most of the basic elements of the API are designed around implementing this, and implementing it well.
        * Data-Driven design allows for different starting configurations and difficulty settings.
* Ms. Pac-Man
    * A classic modification of Pac-Man, Ms. Pac-Man introduced a bunch of features that have become standards of the series including:
    * Features:
        * Multiple mazes (4 to be precise)
        * More stochastic ghost behavior (so it can't be defeated with patterns)
        * Moving fruit collectibles
        * Cut-scenes! (Even with a little bit of plot :wink:)
    * Design Considerations:
        * Strong decoupling of the back-end and front-end allow for different mazes to be implemented with only changes to data
        * Data-driven design allows the AI and maze configuration to be modified without writing new code (and possibly even dynamically!)
        * Cut-scenes can also be implemented with data by defining actors and positions and reading it in *ex post facto*
* Adversarial/Networked Pac-Man
    * A less traditional modification of Pac-Man, adversarial Pac-Man allows you and a friend to each play as Pac-Man and the ghosts in a best of 3, 5, or 7 round ***FIGHT TO THE DEATH!!!***
    * Features:
        * New Power-Ups (Slow-Mo, bonus points, etc.)
        * LAN Play
        * Even more levels (and a level builder :eyes:)
    * Design Considerations:
        * Lightweight and discrete state objects allow for simple and fast transfer of information over LAN
        * Additional power-ups can be implemented in data using the builder design pattern
        * The level builder can be used to graphically "write" new data files
            * Different level-builder "modes" can be implemented so long as they implement the right interfaces

## Test Plan

API Testing Strategy Considerations:

* `Sprite` and `MovingSprite` are designed to be interrogated
    * Anything that extends `Sprite` or `MovingSprite` is designed to proffer its position, type, and orientation, by calling simple public methods, which allows game state to be easily determined and verified during unit testing.
* `Tile` and `TileCoordinates` are designed to provide state when necessary
    * Very similar to the above, this allows for JUnit tests to determine if Tiles are of the right type and characteristics for the level that's being tested.

Test Scenarios:

* Moving Pac-Man
    * Scenario: The player presses the W key, commanding Pac-Man to move up into an empty space. Pac-Man was already facing up.
        * Expected Outcome: Pac-Man moves vertically by one tile into the empty space, playing the "waka" sound effect as he moves.
        * Helpful Design Techniques/Methods:
            * Check original position and final position? Are they correct?
                * Can interrogate the methods of `MovingSprite` to verify this.
            * Set the return type of the play sound method to a boolean to verify if it has been played or not.
    *  Scenario: The player presses the A key, commanding Pac-Man to move left into an empty space. Pac-Man was previously facing right.
        * Expected Outcome: Pac-Man moves left by one tile into the empty space, playing the "waka" sound effect as he moves. He rotates to face this new direction.
        * Helpful Design Techniques/Methods:
            * Check original position and final position? Are they correct?
                * Can interrogate the methods of `MovingSprite` to verify this.
            * Check the original and final orientation to make sure the sprite has rotated to the correct orientation.
            * Set the return type of the play sound method to a boolean to verify if it has been played or not.
    *  Scenario: The player presses the A key, commanding Pac-Man to move left into a space that is occupied by an obstacle. Pac-Man was previously facing right.
        * Expected Outcome: Pac-Man rotates to face the new direction, but doesn't move. Since he didn't move, he doesn't play the "waka" sound effect.
        * Helpful Design Techniques/Methods:
            * Check original position and final position? Are they correct?
                * Can interrogate the methods of `MovingSprite` to verify this.
            * Check the original and final orientation to make sure the sprite has rotated to the correct orientation.
            * Set the return type of the play sound method to a boolean to verify if it has been played or not.

* Moving and Mutating Ghosts
    * Scenario: A relatively "high-skill" ghost is searching for Pac-Man and its RNG result indicates that it should move towards Pac-Man.
        * Expected Outcome:
            * The ghost gets closer to Pac-Man.
        * Helpful Design Techniques/Methods:
            * The position of the ghost can be interrogated using the publicly exposed methods of `MovingSprite`
                * Record the initial and final positions, and make sure that the ghost begins to close the gap over a couple of iterations.
    * Scenario: A relatively "low-skill" ghost is searching for Pac-Man and its RNG result indicates that it should move in a random direction with respect to Pac-Man.
        * Expected Outcome: The ghost should move in a random (valid) direction. This choice of direction should be roughly uniform over many iterations.
        * Helpful Design Techniques/Methods:
            * Seeding and injection testing!
                * These are key techniques for testing anything stochastic, particularly if you want repeatable results.
    * Scenario: Pac-Man eats the Power Pill and puts the ghosts into a "frightened" state meaning that they move erratically and away from Pac-Man.
        * Expected Outcome:
            * The ghosts will make every attempt to move away from Pac-Man, and if contacted by Pac-Man, will be eaten by him.
        * Helpful Design Techniques/Methods:
            * Very similar to the "high-skill" ghost test, this test will exploit the public methods of `MovingSprite` to verify that the ghosts are moving in the right direction.
            * Additionally, fault injection can be utilized to make sure that Pac-Man is able to:
                * Eat the "scared" ghosts (which will re-spawn in the spawn box)
                * Not get killed by the "scared" ghosts

* Eating Things
    * Scenario: Pac-Man eats a "normal" Dot
        * Expected Outcome: The score increases, the dot is removed from the grid, and Pac-Man occupies the space that was previously occupied by the dot.
        * Helpful Design Techniques/Methods:
            * Many of the interrogation methods from above still apply, so no need to rehash them here.
            * Dots are a specific type of `Sprite` and also implement `SpriteObservable` so they have their own set of interrogation methods that can be exploited to check status.
    * Scenario: Pac-Man eats a Power-Up of some variety
        * Expected Outcome: Pac-Man gains the boons associated with that Power-Up.
        * Helpful Design Techniques/Methods:
            * Same observable methods, yet another use case.
            * Power-Ups will cause many of Pac-Man's properties to change quite noticeably, which can also be observed fairly readily.
    * Scenario: Pac-Man eats a ghost without the Power Pill
        * Expected Outcome: Pac-Man dies. Plain and simple.
        * Helpful Design Techniques/Methods:
            * See above. Only real change is that Pac-Man will play the death animation (which should be observable) and the life counter can be checked to verify that it has decremented.

* Using the Level Builder
    * Scenario: The user drags a tile onto the grid
        * Expected Outcome: After verifying that the operation is valid (and throwing an exception if it's not!) the view will update to display the tile in its new location.
        * Helpful Design Techniques/Methods:
            * Individual `Tiles` can be interrogated from the `PacmanGrid` by calling the `getTile(TileCoordinates)` method.
            * If the placement is successful, the operation should return `true` as a simple boolean check.
            * TestFX can be used to automate this test on the view end.
    * Scenario: The user saves a designed level to JSON
        * Expected Outcome: The level is serialized into a standardized JSON format using Jackson, and saved to a user-specified directory.
        * Helpful Design Techniques/Methods:
            * Expected file comparison, particularly for simple (but deeply nested) cases is probably the move here.
            * Jackson has annotations that should make this process relatively easy...we plan on exploiting them.
    * Scenario: The user tries to open a malformed file, a file with missing dependencies, etc. (GIGO case)
        * Expected Outcome: The application gracefully throws one (or several) exceptions in dialog boxes that the user can then read and use to guide their next course of action.
        * Helpful Design Techniques/Methods:
            * File management APIs will handle input validation and throw exceptions when necessary to indicate that they've received bad data.
                * These exceptions can then be compared to expected exceptions using JUnit to verify that the correct types of exception are being thrown at the right time.
            * This can also be tested with TestFX to verify the behavior of the dialog boxes themselves.