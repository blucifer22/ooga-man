# Sprint 2 Presentation

# Run Program

- George: show gameplay demo: `DemoController`
- Show Pacman AI as second mode: `Controller`.  This is probably the best time to talk about AI and tracking.  Make the UI taller.
    - Pause briefly at start so David can briefly talk about the menu being new.
    - Also jump into preferences and change the language to Spanish, go back to the menu, *but make sure the language is English before hitting start game, since the game view hasn't been fully translated yet*.
    - "As each feature is shown, someone on the team should briefly describe how work they did over the past week relates to it"
    - **Features to Note:**
        - Offset ghost release
        - Cherries + dots
        - Back to maze
        - Teleport
        - Animation into Wall (Franklin)

Marc:
- Theme `JSON` file -- especially how animations are just costumes with a frame number suffix
  - Demo the `SpriteAnimationFactory`
- Show tests
    - TestFX: tests in `MainMenuTest` and `PreferenceViewTest` (should be at least 3).
    - JUnit: 5 tests?

# Major Sprint Events (need 2 - Matt)
1. Animations
2. GhostAI changes depending on the gamestate with powerups
3. One of these needs to be a "bad" event!!!

# Teamwork and Communication (Matt)
* What went well?
  * Slack is always super active!
  * Everyone is an overachiever
* What was difficult?
  * Midterms. Lots of midterms.
  * Not good midterms, either.
* Is GitLab helpful?
  * Are papercuts enjoyable???
  * Issues are not helpful. We've largely been creating issues just before we merge things in.
  * Merge requests work well when breaking changes are introduced, but add overhead otherwise.

# Plan for Final Sprint (Franklin)

- Completed features: 
    - Animations!
    - Ghost AI: Ghosts also vary in the amount of time before they leave spawn
    - Stackable powerups that are timed
    - Teleports: one side to another
    - Menus
        - Multiple language support

- Features to implement for final demo
    - Level builder
    - "Full" Adversarial Mode
    - Level progression system
      - Full-stack loading from JSON data
    - More preferences in the preferences window (themes!) 