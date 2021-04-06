# Team Lettuce Leaf: OOGA PLAN Presentation

* Genre/Game:
  * Classic Video Games (Pac-Man + variations)
  * Why Pac-Man?
    * *Underneath all of the animation, PacMan has a grid-based movement system.  It offers a high amount of extensibility in the sophistication of the ghost AIs, power-ups, and opportunities to expand to a second player.*
* [Wireframe](https://www.figma.com/proto/PaLaYIW1uVSMQSaC80ud1k/CS308%3A-Pac-Man?node-id=3%3A106&viewport=1906%2C615%2C1.3213427066802979&scaling=min-zoom&page-id=0%3A1)

## Implementation Plan
  
* Core Features:
  * Sprint 1
    * Classic Pac-Man
      * Implementation of "classic" Pac-Man gameplay elements
        * User control of Pac-Man
        * Basic AI for ghosts
        * Score keeping
        * "Classic" power-ups (`PowerPill`, fruits, etc.)
  * Sprint 2
    * Ms. Pac-Man
      * Implementation of "enhanced" Ms. Pac-Man gameplay elements
        * "Enhanced" AI for ghosts
        * New levels and theming
        * New power-ups (Speed-Up, Slow-Down, PoisonPill, etc.)
    * Level Builder
      * Ability to load/save levels from/to JSON
      * Ability for the user to create their own levels using "themes"
  * Sprint 3
    * Adversarial Pac-Man
      * A new, multiplayer version of Pac-Man where players play against each other as either Pac-Man or the ghosts
      * Best-of-3, best-of-5, and best-of-7 modes
    * User Profiles
      * Track essential user stats, such as high-scores, hours played, achievements achieved, etc.
      * Profiles are stored as JSON and can be moved between installations to take your progress with you


* Extensions and Stretch Goals:
  * Sprint 1
    * Specialization of Ghost Behavior
      * Enable each Ghost to implement a separate logic AI (canonically Blinky, Pinky, Inky, and Clyde)
  * Sprint 2
    * Theme Builder
      * Allows the user to graphically build their own themes instead of having to write JSON files/build out directories by hand.
    * Cut-scenes
      * Fun animated cut-scenes to play in-between level transitions (similar to those found in the original Ms. Pac-Man)
  * Sprint 3
    * Achievements
      * When certain criteria are met, the player can get achievements.
    * Online Leaderboards/Sign-in (Firebase?)
      * Uploads player profiles to a cloud NOSQL database we can set up global leaderboards.
      * Also allows us to enable OAuth2 with relatively little overhead, which is good for application security.
    * LAN Play (***SUPER STRETCH GOAL***)
      * Looking into Kryo to handle this, but may introduce some major technological hurdles beyond the scope of this class.

* Primary Team Roles:
  * Franklin Wei: Running point on back-end
  * Marc Chmielewski: Working with David to link front-end and back-end
  * David Coffman: Running point on front-end
  * George Hong: Running point on implementing movement and Ghost AI
  * Matthew Belissary: Running point on asset acquisition and making Sprites


## Design Plan

* Overall Framework Design
  * Heavily decoupled front and back ends to maximize modularity.
    * The observer design pattern is used extensively throughout the API to facilitate this
    * Backend is has **no external references** to controller or frontend -- all backend-to-frontend communication happens through observer interfaces.
  
* Modules
  * Front-End
    * In charge of the View components that make up the front-end of the game.
      * Utilizes event-driven programming techniques such as listeners to get notified whenever things change in the backend.
  * Back-End
    * In charge of the Model components that define the game's current *state* and *behavior* 
  * Middleware (Controller)
    * Facilitates the linkage of the Front and Back ends, as well as the loading and saving of game files in JSON.
  * Entry-Point (Main)
    * Fairly simple ==> provides an entry point to bootstrap the game and display the splash screen
* API Examples
  * API 1: Observation Interfaces of Sprite state changes
    * Service: informs `SpriteObserver`s that the `SpriteObservable` has changed.  These events can include type changes, visability changes, scaling, translation, and rotation, and inform frontend to render a changed property.
    * Extension: This API is designed for extensibility due to how generally it can be applied to any Sprite in the backend, and allows for a wide variety of animation possibilities, mixed with states.  A few property changes include: `TYPE_CHANGE, SHOW, HIDE, TRANSLATE, ROTATE, SCALE`, which can be matched to the appropriate Sprite or transformation that reflects such a change.  By including a `sender` that also uses an immutable read interface, the Sprite both adheres to interface segregation and can include many game elements besides PacMan, such as the ghost.
    * Design Ergonomics: PacMan utilizes many rapid animation changes, such as orientation of the PacMan sprite while moving up, down, left, or right.  This allows frontend to be notified as soon as possible of a change and render this to the user.
  * API 2: Existence Observer Interface
    * Service: although the previous `SpriteObserver` interface exists, the frontend still needs to know when Sprites come into existence (such as an appearing power-up) and are destroyed (often as a power-up or dot is consumed).  After being notified, the frontend can create or remove the necessary representations. 
    * Extension: Similar to the above API, the `SpriteExistenceObserver` interface can be used for the frontend with a wide variety of `Sprite`s that implement the `SpriteObservable` interface.
    * Design Ergonomics: The inclusion of this API allows for much better organization than using `SpriteObserver` for everything, including the creation and removal of a `Sprite` which is arguably an edge case.  This allows for more focused code on the part of the frontend to render a `Sprite` for the first time or remove it from further consideration.
* Usecase Examples
  * Usecase 1
```java=
package ooga.model;
import java.util.Collection;

public class MarcUseCase {
  public static void main(String[] args) {
    PacmanGameState pgs = null; // In reality this would be instantiated to loaded game level

    Collection<Sprite> sprites = pgs.getSprites(); // Get the current sprites

    if(consumablesRemaining(sprites) == 0) {
      System.out.println("Pac-Man has eaten ALL THE THINGS!!!");
      // TODO: Handle loading the next level or ending the game if this is the last one.
    }
    else {
      System.out.println("Pac-Man still has to eat " + consumablesRemaining(sprites) + " things.");
    }
  }

  private static int consumablesRemaining(Collection<Sprite> sprites) {
    int numConsumablesRemaining = 0;
    for (Sprite sprite : sprites) {
      if(sprite.mustBeConsumed()) {
        numConsumablesRemaining++;
      }
    }
    return numConsumablesRemaining;
  }
}
```
  * Usecase 2
```java=
import ooga.controller.SpriteObserver;
import ooga.model.sprites.Sprite;
import ooga.model.SpriteCoordinates;
import ooga.model.api.SpriteEvent;
import ooga.model.api.ObservableSprite;

/**
 * This use cases tests if pacman hits a cherry sprite using the current methods at the time of
 * writing this code (was able to compile when in the src folder)
 */
public class PacmanHitsCherry {

  // Users score
  private static int score;

  public static void main (String[] args) {

    // A cherry sprite is create to be a stationary sprite with the type "Cherry"
    SpriteObservable cherry = new Sprite() {
      @Override
      protected void step(double dt) {}

      @Override
      protected boolean isStationary() {
        return true;
      }

      @Override
      public String getType() {
        return "Cherry";
      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates();
      }
    };

    // A pacman sprite is create to be a moveable sprite with the type "pacman"
    SpriteObservable pacman = new Sprite() {
      @Override
      protected boolean isStationary() {
        return false;
      }

      @Override
      public String getType() {
        return "pacman";
      }

      @Override
      protected void step(double dt) {

      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates();
      }
    };

    /**
     * If the pacman sprite intersects with the cherry sprite, the score needs to increase by 50,
     * the cherry is then removed from the screen until the cherry is ready to respawn to be
     * collected again
     */

    if (pacman.getCenter().equals(cherry.getCenter())){
      score = score + 50;
      cherry.removeObserver(e -> {
        new SpriteObserver() {
          @Override
          public void onSpriteUpdate(SpriteEvent e) {
          }
        };
      });
      if (cherry.respawn()){
        cherry.addObserver();
      }
    }
  }
}
```
## Alternative Design and Trade-Offs
### Collision handling options:
  * Design: Treat sprites as multi-tile objects, and check for intersection of tiles.
    * Pros: Allows large, complex-shaped moving sprites.
    * Cons: Complex. Makes collision detection more difficult on the model side.

  * Design: Treat sprites as single-tile objects, no matter their graphical size.
    * Pros: Simplifies collision detection on model side. Still allows graphically large sprites.
    * Cons: Prevents functionally large sprites.

