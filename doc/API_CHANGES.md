# API Changes

## InputSource

We incorporated a single action input into InputSource, rather than the enum of available actions we had specified.

## MovingSprite

We renamed MovingSprite -> MoveableSprite.

## PacmanGameState

We renamed addExistenceObserver -> addSpriteExistenceObserver.

We added functionality to load grids.

We removed the separate getMovingSprites() and getStationarySprites() methods.

## PacmanGrid

We left the Iterable interface implementation blank, and opted to loop directly instead.

## Sprite

Major changes -- the getType() method was removed with the addition of animation functionality. The orientation is now represented as a 2-vector.

Step and observer interface stayed the same, minus the addition of Dependency Injection.

Added several methods in relation to start/end of levels and lives -- reset, uponNewLevel().

## SpriteEvent

No changes.

## SpriteCoordinates

Added getTileCenter().
