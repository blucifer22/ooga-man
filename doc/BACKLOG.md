# OOGA Use Cases

### Team Number 4 - Lettuce Leaf

### Names
Franklin Wei (fw67)

David Coffman (djc70)

George Hong (grh17)

Marc Chmielewski (msc68)

Matthew Belissary (mab185)

## Use Cases

### Franklin #1

User presses a key. The frontend receives a JavaFX callback. The frontend then calls the onKeyPress() method of all registered HumanInputObservers to notify them.

The controller will have registered a HumanInputManager with the frontend, and this gets notified of the keypress. It translates this keypress into the right format for consumption by the backend.

The controller will also have registered the HumanInputManager as the backend's human input source. The Pacman sprite will have its input source referring to this human input source. When the next frame step function is called, it queries its InputSource for the requested direction of motion. When it sees that this the user is requesting a move, it updates its velocity vector appropriately.

### Franklin #2

The user starts the game. The Main entry point initializes a Controller. The controller creates a window. The controller invokes a frontend method to run the main menu, which is displayed to the user.

### Franklin #3

The user clicks the "New Game" button in the Main Menu. The user is shown a dialog box to choose a JSON file describing the game they wish to play. The dialog box is shown a text message indicating the following message: "Choose game."

Then, the user chooses a JSON file, which gets passed to the Controller. The Controller reads the JSON game file and extracts the "levels" array from it. It stores the array, and then loads the first level.


The level loader then parses the level JSON. It first loads the theme sheet, which then is passed to the frontend in the form of a Map from Strings to SpriteAppearance objects. Then the grid is parsed by reading from arrays in the file and the tiles are loaded into the  Grid.

Finally, sprites are loaded from the JSON file by reading in their class, parameters, and location.

### Franklin #4

The player eats a dot. The Pacman sprite's step() function calls the findCollidingSprites() method in the GameState (to which it has access through an immutable interface). It then calls hit() on each Sprite with which it intersects.
The dot then destroys itself after increasing the score on the parent PacmanGameState.

### Franklin #5

The player eats a powerpill that grants them the "Speed up"
powerup. This powerup sees that the Sprite by which it was hit is a
Pacman, and so calls the setTemporarySpeed method on Pacman. This
causes the speed of Pacman to increase for a fixed period of time,
after which is reverts to normal.

### Franklin #6

The player gets a RANDOM_TELEPORT powerup. The powerup will search for
an accessible square on the grid, and the Pacman's position gets set
to be that grid location.

The grid location will be found such that there is a minimum distance
between it and the nearest ghost.

### Franklin #7

The user selects the "DEMO" mode from the main menu. The Controller
sets up the model so that all input comes from computer-generated
InputSources. The game runs as usual apart from that, and shows Pacman
playing itself.

### Franklin #8

The user chooses to control a ghost from the main menu. The game
starts with Pacman taking input from an AI input source. The player's
input source is routed to a ghost, which the player then controls. If
the player eats Pacman, it gains points. If the player is eaten by
pacman (i.e. after Pacman gets a power-pill with the EAT_GHOSTS
value), then the player dies.

### Franklin #9

The player decides to load a new level theme. They are presented a
dialog box where they can choose a new JSON file from the data
folder. Once they choose a file, it gets passed to the Controller,
which tells the frontend to update its string-to-sprite appearance
map.

### Franklin #10

The player decides to turn on sound effects. This causes sound events
to be sent by individual Sprites to the GameState's SoundManager. The
SoundManager then issues the appropriate calls to the JavaFX frontend,
which issues API calls to the JavaFX API to play the appropriate audio
files corresponding to symbolic names passed from the model.

***

### Matthew #1

Pac-man is hit by a ghost. This calls the `hit()` method to indicate that Pac-man collided with a ghost. This causes a life to be lost if Pac-Man does not have the "Eat Ghost" powerup. If a life is lost, Pac-Man's position is reset to the starting position as well as the ghosts are placed within the ghost box.

### Matthew #2

The user consumes a powerpill. The `hit()` method is called to indicate that Pac-man hit a powerup. This activates a powerup based on which pill was consumed (the user cannot see which powerups are associated to which powerpill). Based on which powerup was consumed, different attributes are assigned to their particular objects as defined in the Powerup class

### Matthew #3

A new round is started. Pac-Man and Blinky sprites are able to be moved around: Blinky governed by their AI, and Pac-Man controlled by the user. After a duration set within the individual Ghost class, Pinky then Inky then Clyde are able to leave the ghost box to start moving around the game board to stop Pac-Man. This is done by warping the respective ghosts to a position above the box.

### Matthew #4

The user pushes an arrow key. This causes Pac-Man's movement vector (which is stored within the Pac-Man object) is updated based on the corresponding arrow key. Pac-Man's sprite will turn to reflect this change to the movement vector, but they will only move if they are unobstructed.

### Matthew #5

The user consumes a powerpill that allows Pac-Man to eat the ghosts. This causes a change in the sprite PNGs stored within the instances of each ghost to be "frightened". The movement style of the ghosts to be "frightened" as well. The changes to the movement and the sprites will remain this way for a set amount of time before reverting back.

### Matthew #6

Pac-Man is able to eat ghosts and collides with a ghost. This causes the `hit()` method to be called. Since Pac-Man is able to eat ghosts, the collision results in the ghost sprite to update to the "eyes only" sprite and for the sprite to travel back to the ghost box where it bounces around until it rematerializes.

### Matthew #7

Pac-Man travels through one of the tunnels on the sides of the map. This causes Pac-Man's position to be wrapped around to the corresponding tunnel exit on the other side of the map. Pac-Man's movement vector is not change but his position is changed to be on the other side of the warp tunnel. This movement is done by changing Pac-Man's internal position variables within the object.

### Matthew #8

The user presses the menu button. This changes the stage that is currently displayed so that it is the menu stage again. This stops the current game from being played and deletes any saved states associated with the game. When the state is changed to the menu, a new game can be started if the user desires or a new gamemode can be chosen.

### Matthew #9

Pac-Man consumes all of the dots for a given level. This is determined by checking all sprites on the screen. If any sprite has a tag "mustBeConsumedToAdvanced", the level continues. If no sprites are found with this tag during the game loop, the level is completed and the player will advance to the next level.

### Matthew #10

Pac-Man consumes a Cherry. This is done by calling the `hit()` method. The cases where Pac-Man consumes a cherry is called and the approprite increase to the score is given. The cherry sprite then disappears from the screen for a certain before it regenerates and reappears in the same position.

***

### George #1
Pac-Man and Ghosts move exactly according to the "tracks" defined by the grid and exhibit no drift.  When the user chooses to pivot the Pac-Man character near corners, Pac-Man does not make the turn until it's as close as possible to the center.

This is done by checking position `x` and `x + v * dt` surrounds the center of the block before registering the turn.  Prior to making the turn, the Sprite snaps to the center of the block to ensure consistency.

### George #2
While moving down a horizontal path, the user is able to press an arrow key into the wall without slowing down or stopping Pac-Man.  When Pac-Man encounters the next intersection it will move in the direction previously chosen.


### George #3
The user tries to run from the ghosts, and continues to eat the dot.  As the user eats the dots in succession, the slowdowns become noticeable and the ghosts catch up to the user.  The `hit` method helps detect a collision between the pac-man and the dot, and it should change pac-man's speed for a short period of time, likely handled by a timer.

### George #4
For Ms. Pacman, when the user presses the arrow key into the wall, the action is no longer registered unless the user presses the arrow key close to the intersection.  Additionally, Ms. Pacman does not stop while it can proceed down a track.

### George #5
The user wants to generate a new level from scratch.  He chooses to go to the *Stage Builder* and clicks on the *new* button.  Afterwards, the user is able to specify a height and width, allowing him to create a massive, expansive stage with many more dots and ghosts.

### George #6

The user, Tommy, is uncertain of his WiFi connection but wants to play with his friend who is right beside him. He chooses to go to the *Versus* mode and select the *Local* option which allows him to play on a single keyboard. Tommy and his friend Christophorus use the *WASD* keys in addition to the arrow keys to alternate playing the ghosts and pac-man. This involves changing the controller of one of the ghosts to `Human` at a time, as well as ensuring that a separate `Score` in `PacmanGameState` to determine total number of wins is now displayed.

### George #7

Because there are 4 ghosts and only a single player controlling all 4, at any time, a user playing in adversarial mode will only control one at a time, with the other ghosts automatically moving. To jump between ghosts, the user presses a key (either `Q` or `M`) to jump between ghosts. In the HUD, one can observe that the icon of the ghost that is currently being controlled is enlarged to distinguish that it is the currently selected ghost.  This is done by swapping `controllers` of the Ghost objects.

### George #8

After the user consumes the power-pill, Pac-Man has a limited amount of time to eat all 4 ghosts.  Each ghost eaten grants 2x the previous amount of points, starting with 200, 400, 800, and finally 1600 if the user is able to act quickly enough.  This can be implemented alongside the power-up state which can last with a timer.  The number of ghosts eaten during this time period can be tracked with a counter.

### George #9
Pac-Man ghosts occasionally trigger a *Scatter* mode which occurs without the user consuming a candy power-up.  At this stage, ghosts can reverse direction, something that is typically not observed and is a clear indication of a stage change from *Chase* to *Scatter*.  This can be implemented by changing the controller of each `Ghost` object into a `ScatterMode` controller that each references a target coordinate for `Ghost`s to cycle around until the stage changes back to *Chase*.

### George #10
In Ms. Pac-Man, fruits bounce randomly around the maze, entering and possibly leaving through the tunnels surrounding the map.  This can be done as a `MovingSprite` that maintain an x-velocity and y-velocity.  Inside the `step` function, the exact position can be used to ensure that the velocities are changed when it comes into close contact with a wall.

***

### David #1
- The game state advances, without user input (i.e. Pacman moves forward).
    - The backend increments or decrements the Pacman `Sprite`'s tile offset (depending on Pacman's orientation).
    - The update to the `Sprite` triggers a notification to the `Sprite`'s observers:
    ```java=
    // in Sprite.java
    for (SpriteObserver so: observers) {
    so.onSpriteUpdate(new SpriteEvent(this, EventType.TRANSLATE));
    }
    ```
    - The observing `SpriteView` then translates to the new position of the `Sprite` in response to the `SpriteEvent`.

### David #2
- The user presses an arrow key at a valid turning time, making Pacman rotate to the left or right.
    - The backend then increments or decrements the Pacman `Sprite`'s orientation (depending on the arrow key pressed).
    - The update to the `Sprite` triggers a notification to the `Sprite`'s observers:
        ```java=
        // in Sprite.java
        for (SpriteObserver so: observers) {
            so.onSpriteUpdate(new SpriteEvent(this, EventType.ROTATE));
        }
        ```
    - The observing `SpriteView` then rotates to the new orientation of the `Sprite` in response to the `SpriteEvent`.


### David #3
- Pacman hits a ghost-eating powerup, turning all of the ghosts blue.
    - The backend changes the `Sprite` type of all ghosts to "panic_ghost" (subject to change).
    - The update to the `Sprite` triggers a notification to the `Sprite`'s observers:
        ```java=
        // in Sprite.java
        for (SpriteObserver so: observers) {
            so.onSpriteUpdate(new SpriteEvent(this, EventType.TYPE_CHANGE));
        }
        ```
    - The observing `SpriteView` then changes its image in response to the `SpriteEvent`.


### David #4
- Pacman eats a powerup, causing the powerup to disappear.
    - The game state object recognizes that the powerup has been consumed.
    - Upon recognizing that the powerup has been destroyed, the game state notifies its `SpriteExistenceObserver`s that the Sprite has been destroyed:
        ```java=
        // in a TBD backend class
        for(SpriteExistenceObserver seo: observers) {
            seo.onSpriteDestruction(destroyedSpriteObservable);
        }
        ```
    - The observing `GameView` then de-renders the destroyed `SpriteObservable`.


### David #5

### David #6

### David #7

### David #8

### David #9

### David #10

***

### Marc #1

The player opens up the game and reaches the main menu. They then enter the game select menu and select "Classic Pac-Man" and initiate a game.

The View will register a click on each of the buttons in the menus as the user navigates the menu system. Upon clicking "Classic Pac-Man" a game of "classic" Pac-Man will be initialized. This will load the default "level_1.json" and initiate game play.

### Marc #2

The user opens up the level builder and loads in an existing level for modification.

The level_NUMBER.json file is selected from a file-picker and then loaded into the level editor utilizing utility Jackson serialization and deserialization methods. The tileset for the level is also loaded in and can be used to make edits.

### Marc #3

Johnny is a small child who has never played video games before. He opens up the game and is kind of confused, so he opens up the help menu to learn how to play the game. He finds the rules a bit confusing so he looks mostly at the pictures and is able to figure it out pretty easily.

### Marc #4

Toru Iwatani (the guy who invented Pac-Man!) is really excited to play our version of the game. He'd much prefer to play it in Japanese, however, and would like to set the localization settings as such. Thus, he goes into the preferences panel and selects the Japanese language option. The game translates itself and he's ready to play.

### Marc #5

The player consumes a "stacked" power-up, that is, one that has the effect of multiple power-ups combined. The two power-ups have the same duration, but different effects as specified in the level JSON.

Ex.) Pac-Man eats a power-up that gives him the ability to eat ghosts and move at double speed for 10 seconds.

Ex. 2) Pac-Man eats a power-up that gives him the ability to shoot lasers, but it slows him down to half-speed for 13 seconds.

### Marc #6

The player completes a game and they get a new high score! The high score value of their profile is updated to this new value and pushed to the database so it can be seen by other players.

Additionally their profile in the database is updated to reflect the total number of games they've played, points they've scored, lives they've lost, and other essential stats.

### Marc #7

The player wants to play adversarial Pac-Man with their friend who lives in the same apartment building. Knowing that they're on the same network, they select the LAN-play option in the main menu. The player decides to be the host and the friend decides to be the guest. After the guest enters the code provided by the host, the host joins the game playing as Pac-Man and the guest joins the game playing as the Ghosts. After each round they swap roles, and the player with the highest score at the end wins.

### Marc #8

The player is playing a game but has to get up and take a phone call so they pause the game. This opens up a menu that says "Continue" and holds the game in a spinlock until the player finishes their phone call and decide whether they want to continue playing or not. If they do, they can click yes and this will resume the game where they left off (with a countdown so they have time to get their bearings?) if not, the game is ended and no stats are recorded.

### Marc #9

The player decides they want to kick-off their video game modding career by making a cool new theme for the Lettuce Leaf version of Pac-Man. To do this, they first make a new folder for the new theme. Then, they find a set of images online to assign to all of the requisite sprites and add them to the `images` folder of the new theme. Finally, they write a `NAME_OF_THEME.json` file to map the paths of these images to the right keys and assign appropriate dimensions like the following example:

```json=
{
  "pacman_open_mouth": {
    "image" : "themes/classic_theme/images/pacman_open_mouth.png",
    "width" : 1.5
  },
  "pacman_closed_mouth" : {
    "image" : "themes/classic_theme/images/pacman_closed_mouth.png",
    "height" : 1.5
  },
  "vertical_line_tile" : {
    "image" : "themes/classic_theme/images/vertical_line_tile.png"
  }
}
```
etc.

### Marc #10

The player is playing any game version and completes a level by eating all of the `Edible` things on the screen. At this point, the level clears, and the score gets a "level complete" bonus.

In the Ms. Pac-Man and Adversarial Pac-Man implementations, this could be supplemented with a fun little cut-scene or animation on the maze, as was the case in the original Ms. Pac-Man.
