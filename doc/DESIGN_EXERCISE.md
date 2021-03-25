# OOGA Lab Discussion
## Names and NetIDs

Marc Chmielewski (msc68)
George Hong (grh17)
Franklin Wei (fw67)
David Coffman (djc70)
Matthew Belissary (mab185)

## Fluxx

### High Level Design Ideas

Overall, we want a Card abstract class that is extended to form KeeperCard, GoalCard, ActionCard, and RuleCard subclasses. These different subclasses represent the different types of cards that can be drawn and played by a `Player`. Each card has a title and its description as well as any special attributes depending on the card type. There will be a `Deck` class that holds the shuffled cards to be drawn by the players. `Player` is able to know what cards it has, what cards are in play, and what cards are discarded. The rules of the game are dynamically updated as new `GoalCard` objects are played.

### CRC Cards

***


```java=
abstract class CardModel {
 String title;
 String description;

 abstract void playCard(Player byWhom);
}
```

``` java=
class KeeperCard extends Card{
    void setType(String typeName);
    String getType();
}
```

```java=
abstract class GoalCard extends Card{
    String winCondition;
}
```

```java=
class FixedCardSetGoalCard extends GoalCard{
    boolean handWins(List<Card> playerHand);
}
```

```java=
class RuleCard extends Card {
    void executeRule();
}
```

``` java=
class CardView {
    CardView(CardModel model);
    Image cardImage;
}
```
***

### Use Cases

 * A player plays a Goal card, changing the current goal, and wins the game.

In FluxxGame::Controller, the game is instantiated and then first turn is queried.

```java=
  List<Player> players = List.of(new HumanPlayer(), new AIPlayer());
  List<Class<? extends Card>> cardTypes = List.of(DogKeeperCard.class, IceCreamKeeperCard.class, ...);
 FluxxGame game = new FluxxGame(players, cardTypes);

 game.startGame(); // Starts a new game of Fluxx

 game.playTurn(); // loops through players, asks for moves
```

Meanwhile, in FluxxGame::playTurn, the active players are iterated over and queried for their next move.

```java=
 applyDrawRules();

 for(Player p : getPlayers()) {
     for(int i = 0; i < numCardsToPlay(); i++) {
         Move m = p.getMove();
         m.execute(this);

         checkEndConditions(p);
         applyRules(getPlayers());
    }
 }
```

 * A player plays an Action card, allowing him to choose cards from another player's hand and play them.

On line 4 of the listing above, the Move object returned is of class StealCardMove, and when executed it performs the "stealing" operation.

 * A player plays a Rule card, adding to the current rules to set a hand-size limit, requiring all players to immediately drop cards from their hands if necessary.

This is incorporated in the code listing above -- upon the execution of a RuleCard, the applyRules() function immediately applies the rules to all players.

 * A player plays a Rule card, changing the current rule from Play 1 to Play All, requiring the player to play more cards this turn.

The new Rule card will update internal game state that causes the numCardsToPlay() function to change value, causing the for loop to iterate for more iterations. This causes all cards to be played.

 * A player plays a card, fulfilling the current Ungoal, and everyone loses the game.

This is done in checkEndConditions(), and will change all `Player` status to be losing.

 * A new theme for the game is designed, creating a different set of Rule, Keeper, and Creeper cards.

This is also accommodated in the code below:

```java
  List<Class<? extends Card>> cardTypes = List.of(DogKeeperCard.class, IceCreamKeeperCard.class, ...);
```
