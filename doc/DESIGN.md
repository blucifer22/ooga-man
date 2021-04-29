# DESIGN Document for OOGA Team 4: PacMan Game Engine

## Names:
* Matthew Belissary (mab185)
* Marc Chmielewski (msc68)
* David Coffman (djc70)
* George Hong (grh17)
* Franklin Wei (fw67)


## Role(s) -> NEEDS ELABORATION
* Back-end:
    * Matthew:
        * Ghost sprites, their state AI, and power-ups
    * Marc:
        * JSON serialization and deserialization, tests, different game modes, and level builder
    * George:
        * Movement system, AI algorithms for Pac-Man and ghosts, different game modes, and level builder
    * Franklin:
        * Audio and Animations for sprites. Game start/end behavior and animations.

* Front-end:
    * David:
        * All menus, buttons, and views displayed in the game. Also languages, mods, and themes.


## Design Goals

### Functionality Objectives

*  Classic Pac-Man Gameplay

We aimed to implement the majority of gameplay elements present in the classic Pac-Man game, such as basic gameplay mechanics, animations, sound effects, and ghost behaviors.

We did _not_ aim for a pixel-perfect emulation or recreation of the original game. Minor details present in the original game, such as a miniscule speed-up applied to Pac-Man when "cornering", were intentionally omitted, as we felt that implementing those would not contribute to the overall gameplay experience, nor would they be a good demonstration of design flexibility.

* Chase Mode Gameplay

The objective of this game mode was to demonstrate that the design was extensible, by creating a mode in which Pac-Man had his input source swapped to a non-traditional AI, and the Ghosts had their input sources swapped dynamically at run-time. This necessitated the development of the `InputSource` interface and its various subclasses, the details of which will be explored later in this document and in the ANALYSIS.

* Adversarial Mode Gameplay

The objective of this game mode was to create an environment in which multiple input sources would need to be parsed and swapped concurrently, which would require careful design, adherence to the observer patterns, and general good programming chops. This was done with the intent that ideally, if this mode were to be designed well, it would be able to inherit a good deal of behavior from the other game modes and require relatively little code to implement. Fortunately this proved to be case and the entire `PacmanGameStateAdversarial` class runs just under 90 lines of code with full documentation!

* Level Builder

The objective of this feature was to create a tool that would allow the team to quickly create data-files for purposes of both testing and level creation as well as to properly stress-test the design of our model classes for level state. To create an effective level builder, we would have to maintain core class data for each element of the level in a format that could easily be serialized to and deserialized from JSON, and design our architecture to be in a position where it could leverage the power of Java reflection to dynamically instantiate the right Sprites under the right conditions.

### Architectural Objectives

* Event-Driven Design
    * The communication of application state happens on an event-driven basis.
        * On front-end, this translates to a UI/UX that is driven by user interaction events like...
            * Mouse clicks
            * Key presses
        * On back-end, this translates to the passing of "Event" objects over defined observable interfaces to communicate state like...
            * `GameEventObserver`
            * `AudioObserver`
* Dependency *Inversion*
    * Interfaces and the Observer Design Pattern were gratuitously applied throughout the code to keep the codebase ad modular as possible throughout the development process.
        * A textbook example of this was the implementation of `GridRebuildObserver` and `GridRebuildObservable` which allowed the complete abstraction of the back and front-end implementations of Grid, so long as they upheld these interfaces.
            * This also allowed for observers to be dynamically attached to and removed from back-end objects which furthered our goals of Event-Driven Design and Dependency Injection.
        * Another example of this is our implementation of the `InputSource` interface and the various and sundry classes that could could implement it and be attached to `MovableSprite` objects.
* Dependency *Injection*
    * The application is capable of having dependencies loaded at runtime and classes instantiated/modified accordingly.
        * For example, levels, which require different classes to be instantiated depending upon their composition and use reflection to facilitate this goal.
* Interface Segregation
    * The use of `ImmutablePlayer` enforces interface segregation so that the view is only given the read-only interface that it needs to display player scores.
    * The use of `MutableGameState` allows `Sprite` objects to make adjustments to the `GameState` upon events such as collisions without exposing all methods associated with the concrete game state.  The `PacmanGameState` is further protected with the use of the `GameStateObservable` interface.

## Easy to Add Features
* New AIs.


### Quality Assurance Objectives
* 80% line-coverage on concrete, model classes
* 50% line-coverage on front-end, view classes
* Graceful handling of exceptions (no crashes or red text!)

## High-Level Design

### Frontend Core Classes
* `:)`
### Backend Core Classes
* `PacmanGameState`: This class is responsible for maintaining the current level and the rules of the current Pac-Man game mode.  It contains the main update loop of the game (iterating through the `Sprite` objects), and is responsible for applying any game state transitions that may occur (such as game over or proceeding to the next level).  This class interacts closely with the `Player` class to keep scoring data for different modes.
* `Sprite` and `Tile`: These classes are provided to build the Pac-Man level.  Sprites consist of objects the player interacts with, including consumables such as dots, cherries, or power-up pills and also the `Ghost` and `Pac-Man` objects.  Tile objects encode the background and which spaces in the game grid can be occupied by the various types of sprites.
    * `Tile` objects live in the `PacmanGrid` object.
* `GhostAI` and classes implementing `InputSource`: classes implementing `InputSource` provide a stream of input events, whether user or algorithm-generated, that can be read by the corresponding sprite objects.  This allows all  to flexibly and quickly customize update and movement behavior.
* `LevelBuilder`: This class provides the backend API to build a new level, providing methods to choose initial grid configuration, toggle through tile types, and insert/delete Sprites.  This class makes use of the `Palette` class to display sprites options to insert into the game.
* `JSONController`: this class is responsible for loading Pac-Man JSON files containing the game mode and level configuration information, and beginning the execution of the game.
    * Both the `LevelBuilder` and loading the JSON makes extensive use of the `-Description` classes.  These classes provide the means for various game assets, such as the grid or sprites to be loaded into the game.
* `AUDIO/SFX`



## Assumptions or Simplifications

* Classic Pac-Man:
    * Pac-Man starts with 3 lives
    * When all dots are consumed, MovableSprites increase in speed up to a point
    * **Pac-Man is controlled by WASD**

* Chase Pac-Man:
    * Each game is only 1 round
    * Pac-Man wins upon surviving 45 seconds.
    * Ghosts win upon catching Pac-Man.
    * **Ghost is controlled by WASD**
        * **Ghosts can be swapped with the Q key**
    * The currently controlled ghost is visually indicated in a blinking green/yellow style.

* Adversarial Pac-Man:
    * Each game is only 1 round
    * **Pac-Man is controlled by WASD**
    * **Ghost is controlled by IJKL**
        * **Ghosts can be swapped with the U key**

* Pac-Man Level Builder:
    * After any stage of the level builder has been completed, the changes are *committed* and cannot be reverted without starting over.
    * There will only ever be one Pac-Man on the grid at any point in time.
    * The Level Builder does not currently support teleporters; those must be added manually in the sprites array in the JSON.
    * The Level Builder assumes that you are building levels for the CLASSIC game mode; alternative game-modes can be specified by altering the JSON.

## Changes from the Plan

* General:
    * The number of frames in each animation type (i.e. Pacman's chomping, ghost wiggling "feet") cannot be set in a theme, and is instead defined in Java code.
        * This was a consequence of how we opted to implement the JSON themes and our later realization that storing individual frames in that file would become incredibly cumbersome.
    * We opted not to implement the network features (LAN play, player database/statistics, etc.) since they would have expanded the scope of the project beyond the realm of feasibility for a team of 5 in a month.
        * The design is such, however, that these features could be implemented in a reasonable period of time and without massive refactoring.
    * We decided to just leave the maze graphics as solid blocks as opposed to implementing unique costumes for each wall type.
        * We elected to do this as we felt it did not demonstrate significant extensibility of the code that not already demonstrated elsewhere, and would have been a massive time-sink to implement.
    * We opted to implement ghost-specific AIs as opposed to one general Ghost AI with tunable parameters.
        * We opted to do this as it would be more reflective of the original Pac-Man game.

* Classic Pac-Man:
    * Eating ghosts does not display their point value. We decided that this was not worth implementing, as it would have been of minimal impact to gameplay.

* Chase Pac-Man:
    * Added this mode instead of Ms. Pac-Man
        * Pretty much what it says on the tin. We opted to do this as we felt that Ms. Pac-Man was more of a "mod" than a separate game mode unto itself, and ultimately didn't serve to show much extensibility beyond which was already demonstrated in our front-end.
        * It's also a great test of the input-swapping features of the `HumanInputManager` and a ton of fun to play!

* Adversarial Pac-Man:
    * Single round instead of multiple rounds
        * Speeds up game-play and gives a bit more objectivity to the scoring system between Pac-Man and the Ghosts
        * Since players are going to share a keyboard, they can swap on their own pretty easily.

* Level-Builder:
    * The level builder currently only natively supports classic Pac-Man levels without teleporters
        * We opted to make this simplification primarily due to time constraints, but future implementation of these features would be relatively trivial.
            * Detailed instructions for how to manually change game modes and add teleporters are provided in the "New Levels" section below.

## How to Add New Features

### New Levels

For "Classic" Pac-Man levels you can use the level builder! Just select the "Level Builder" option on the main menu.

#### Level-Builder: Grid Configuration

* On the first screen you can set the dimensions of the grid by moving the sliders and the state of the tiles by clicking them.
    * Black tiles are "empty" and can be traversed by both Pac-Man and the Ghosts
    * Red tiles are "permeable" and can *only* be traversed by Ghosts. Pac-Man cannot move through these blocks.
    * Blue tiles are "blocked" and cannot be traversed by anyone.
* Once the grid is set up to your liking, click "Next" to move to the sprite placement stage of the level builder.


![A demo of the TILING phase of the level builder](https://i.imgur.com/g1YKXVs.png)


#### Level-Builder: Sprite Configuration

* To place sprites, select the type of Sprite you would like to place from the combo box.
    * Left-click on the tile you would like to place the Sprite and one instance of the Sprite will populate the tile.
    * Sprites can be *stacked*, meaning multiple Sprites can populate the same tile!
        * *NOTE: Only one Pac-Man Sprite can be placed per level!*
* To remove Sprites, just right-click on the tile you would like to remove the Sprites from.
    * *NOTE: This will remove ALL Sprites from the tile*

![A demo of the SPRITE_PLACEMENT phase of the level builder](https://i.imgur.com/NMEODG3.png)

#### Level-Builder: Saving Levels

* Once your level has been fully configured, you can save the level to a JSON file by clicking the "Next" button.
    * This will open up a file-chooser. Input a valid JSON filename and then click save to save the level!
    * You can then select this saved level when you click "Start Game" at the main menu and playtest it!

![A demo of the SAVING phase of the level builder](https://i.imgur.com/9a6d33z.png)

![Playtesting the new level!](https://i.imgur.com/FeelAG5.png)

#### Level-Builder: Miscellaneous Notes

**The level builder does not yet natively support teleporters.**
* If you want to add teleporters to your level, you will need to do so manually within the JSON, as shown below.
    * Teleporters are added pairwise, and linked by having the same exact "TELEPORTER_\*" inputSource String.
    * In the below example we used "TELEPORTER_A", so the next logical option would be "TELEPOERTER_B" and so on and so forth.

```json=
{
  "className": "TeleporterOverlay",
  "inputSource": "TELEPORTER_A",
  "coordinates": {
    "position": {
      "x": 0.5,
      "y": 14.5
    }
  }
},
{
  "className": "TeleporterOverlay",
  "inputSource": "TELEPORTER_A",
  "coordinates": {
    "position": {
      "x": 27.5,
      "y": 14.5
    }
  }
},
```

**The level builder currently only natively supports "CLASSIC" Pac-Man**

* To implement a level in ADVERSARIAL mode, simply change the JSON gameMode to ADVERSARIAL. No further action is needed.

Ex.)

```json=
{
  "gameMode": "CLASSIC",
  "gridDescription": {
    "gridName": "",
    "width": 28,
    "height": 31,
    "grid": 
```
...will become...

```json=
{
  "gameMode": "ADVERSARIAL",
  "gridDescription": {
    "gridName": "",
    "width": 28,
    "height": 31,
    "grid": 
```

* To implement a level in CHASE mode you will need to:
    * Change the JSON gameMode to CHASE
    * Set a Pac-Man AI as Pac-Man's inputSource (PacmanBFSAI works best from our experience)

Ex.)

```json=
{
  "gameMode": "CLASSIC",
  "gridDescription": {
    "gridName": "",
    "width": 28,
    "height": 31,
    "grid": 
```

...will become...

```json=
{
  "gameMode": "CHASE",
  "gridDescription": {
    "gridName": "",
    "width": 28,
    "height": 31,
    "grid": 
```

...and...

```json=
 {
  "className": "PacMan",
  "inputSource": "PLAYER_1",
  "coordinates": {
    "position": {
      "x": 13.5,
      "y": 23.5
    }
  }
},
```

...will become...

```json=
 {
  "className": "PacMan",
  "inputSource": "PacmanBFSAI",
  "coordinates": {
    "position": {
      "x": 13.5,
      "y": 23.5
    }
  }
},
```

### New Themes ("MODS")

At the highest level, a "theme" is a mapping from asset names to asset files (such as images and sounds). As such, the implementation of new themes is primarily handled by the creation a JSON file that maps from these assets to their respective names. A couple of excerpts from the `theme.json` for the "Classic" theme is attached below to demonstrate this.


This portion of the JSON handles the naming of theme, which is what will display in the ComboBox on the preferences menu, and also assigns aliases and metadata to the various costumes in the game.
```json=
{
  "name": "Classic",
  "stylesheet": "themes/classic/theme.css",
  "costumes" : {
    "blinky_up_1" : {
      "fill" : "themes/classic/images/ghosts/blinky/blinky_up_1.png",
      "imageFill" : true,
      "scale" : 1.0,
      "bottomHeavy" : true
    },
    "blinky_up_2" : {
      "fill" : "themes/classic/images/ghosts/blinky/blinky_up_2.png",
      "imageFill" : true,
      "scale" : 1.0,
      "bottomHeavy" : true
    },
    "blinky_down_1" : {
      "fill" : "themes/classic/images/ghosts/blinky/blinky_down_1.png",
      "imageFill" : true,
      "scale" : 1.0,
      "bottomHeavy" : true
    },
```

Meanwhile, this section of the JSON handles the last couple of costumes and the mapping of the sound effect filepaths to their aliases.

```json=
"ready_1" : {
      "fill" : "themes/classic/images/general/ready_1.png",
      "imageFill" : true,
      "scale" : 3.0,
      "bottomHeavy" : false,
      "rotates": false
    }
  },
  "sounds": {
    "button-click": "themes/classic/sounds/button.mp3",
    "frightened-loop": "themes/classic/sounds/frightened_loop.wav",
    "fruit-eaten": "themes/classic/sounds/fruit_eaten.wav",
    "ghost-eaten": "themes/classic/sounds/ghost_eaten.wav",
    "ghost-eyes": "themes/classic/sounds/ghost_eyes.wav",
    "normal-loop": "themes/classic/sounds/normal_loop.wav",
    "pacman-chomp1": "themes/classic/sounds/pacman_chomp1.wav",
    "pacman-chomp2": "themes/classic/sounds/pacman_chomp2.wav",
    "pacman-death": "themes/classic/sounds/pacman_death.wav",
    "start-classic": "themes/classic/sounds/start_classic.wav",
    "start-mspacman": "themes/classic/sounds/start_mspacman.wav"
  }
```

Naturally, the assets themselves should be added to the appropriate folders as specified by what you put in the `theme.json`. We have also established some conventions for what costumes should be named to support our in-built animation infrastructure. For sake of brevity, we won't enumerate them all here, but they should have the same overall directory structure and nomenclature as the "classic" theme, so a modder could use that as a guide if they wanted to.

Finally, the actual look-and-feel of the program is handled by the `theme.css` file. An example for the "Classic" theme is shown below.

```css=
@import url('https://fonts.googleapis.com/css2?family=VT323&display=swap');

.view * {
  -fx-font-family: VT323;
}

.menu-title {
  -fx-font-size: 80px;
  -fx-font-weight: bold;
  -fx-text-fill: linear-gradient(#ffa500, #f5dc00);
  -fx-corner-radius: 5px;
}

.styled-button {
  -fx-font-size: 40px;
  -fx-font-weight: bold;
  -fx-background-color: linear-gradient(#ffa500, #f5dc00);
  -fx-text-fill: white;
  -fx-border-radius: 5px;
  -fx-background-radius: 10px;
  -fx-stroke: black;
  -fx-stroke-width: 2px;
  -fx-label-padding: 0px 5px 0px 5px;
}

.styled-label {
  -fx-text-fill: linear-gradient(#ffa500, #f5dc00);
}

.styled-label-body {
  -fx-font-size: 16px;
}

.styled-label-heading {
  -fx-font-size: 16px;
}

.menu-button-box {
  -fx-spacing: 10px;
}

.view {
  -fx-background-color: black;
}

.card-pane {
  -fx-vgap: 10px;
}

.scoreboard {
  -fx-hgap: 5px;
}

.card {
  -fx-background-color: linear-gradient(#ffa500aa, #f5dc00aa);
  -fx-background-radius: 5px;
  -fx-padding: 5px;
  -fx-font-size: 20px;
}

.styled-label-dropdown-label {
  -fx-text-fill: white;
}
```

This is fairly typical CSS, so if you have any web development experience this should seem fairly familiar. To implement a new mod, you can just copy this into a new `theme.css` and modify the parameters as you see fit to accomplish the look-and-feel that you desire.

### New Animations

New animations can be added by assigning new values to the `SpriteAnimationType` enum in the `SpriteAnimationFactory` class. Some examples of this are shown below.

```java=
public enum SpriteAnimationType {
    /**
     * Ghost moving up.
     */
    GHOST_UP(true, "up", 2),
    /**
     * Ghost moving down.
     */
    GHOST_DOWN(true, "down", 2),
    /**
     * Chomping pacman.
     */
    PACMAN_CHOMP(true, "chomp", 3, 1.0 / 20.0, FrameOrder.TRIANGLE),
     /**
     * Blinking powerpill.
     */
    POWER_PILL_BLINK(true, "blink", 2, 1.0 / 6.0, FrameOrder.SAWTOOTH),
```

As you can see, we have implemented several different types of constructors for the enum, so we can support many different types of animations. For example, the two GHOST animations that are shown here are sprite specific, meaning that they will look for a particular Sprite name in the schema when assembling the animation frames. The CHOMP animation is an example of a "TRIANGLE" animation type which will iterate back-and-forth over a set of frames, (i.e. 1->2->3->2->1...), while the POWER_PILL_BLINK animation is an example of the "SAWTOOTH" animation type which will just linearly iterate over a set of frames. (i.e. 1->2->3->1->2->3...). We have also overloaded the constructor with a variety of other parameter combinations to make the animations virtually infinitely customizable.

In summary, all you need to do to add a new animation is:
* Package your properly named files into a properly named folder (see existing animations as a guide)
* Add a new member to the `SpriteAnimationType` enum that contstructs the animation.
* Construct a `SpriteAnimationFactory`, and query it for `ObservableAnimations` as you see fit.

### New SFX

Adding new sound effects is even easier than adding animations. With respect to asset management, much like animations, just create a new file in the sounds folder of the theme that you're modifying and you're set.

On the back-end, all of the sound-effects are marshalled through the `AudioManager` class, so all you need to do is pass in the id that you've assigned the sound effect in the `theme.json` and call the `playSound` method.

For ambiance, that is, audio that plays on loop in the background, you can either call `setAmbiance` directly, or add a new case to the `onGameEvent` method to set the new ambiance to trigger on a particular type of `GameEvent`.

The classic example of this is how we manage the audio transitions when Pac-Man eats a PowerPill and the ghosts go from the "NORMAL" to the "FRIGHTENED" state. After this transition, the audio changes by passing in the `FRIGHTEN_ACTIVATED` `GameEvent`, and returns to normal by passing in the `FRIGHTENED_DEACTIVATED` `GameEvent`.