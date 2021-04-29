# OOGA Final Demo!
### Introduction
* Introductions of teammembers and team roles
    - We're team lettuce leaf for OOGA Salad, and we're...
        - I'm _______ and my contributions _________


### Functionality
#### Playing the Game:
* TEAMMEMBER will run ooga.Main.java to initaite the game. Upon game start up **David** will explain the functionality of the different menus and Frontend elements (Choosing different themes etc)
* It may be helpful to use an unobtrusive virtual keyboard to demonstrate Sticky keys, close to the original game you know and love, talk about how all the modal behavior of Pac-Man is present in the game, demonstrate teleporters, etc, talk about all the display elements present, including score and lives.  Fun facts about ghost personalities?
* TEAMMEMBER will select classic PacMan. **Matthew** will explain the basics of sprite movement, GhostAI, and powerups (speedup and slowdown too) -> PacMan should win to show level progression then die...talk about increase speed in difficulty for ghosts, test of endurance
* TEAMMEMBER will select chase PacMan. **George** will explain the basics of how the gamestates differ from a design standpoint -> Ghosts should win to the "Ghosts Win" sprite
* TEAMMEMBER will select adversarial PacMan. **Franklin** will explain the basics of how the gamestates differ from a design standpoint -> PacMan should win

#### Level Builder and Changing Files
* TEAMMEMBER will select Level Builder. **Marc** will explain how to build a small test level, and will then display it
* TEAMMEMBER will open up a JSON file of the level builder. **SOMEONE** can show how to add a teleporter, changing the gamemode, and ghost spawn? (some other change to the gamefile)

#### Testing and Coverage
* TEAMMEMBER will conclude the Functionality portion by showing the Tests and that they all pass
    * **David** will highlight the frontend tests and their coverage
    * **Matthew** will highlight the backend tests and their coverage -- show LevelBuilder tests

### Design
#### Deviations from the plan and Design Goals:
* TEAMMEMBER will show the original design plan
    * **George** will discuss how what parts of our code are flexible like our design plan intended
        - Easy inclusion and swapping of new AIs
        - Fish bowl mode
        - Power-ups?
    * **Marc** will discuss how our core code is closed and data-driven

#### Highlighting APIs:
`SpriteObserver.java`, `SpriteEvent.java`
* TEAMMEMBER will show either SpriteObserver or SpriteExistenceObserver (from the first presentation)
    * **Franklin** will discuss how this API is able to accessed in a way that is open for extension and how it can be used and how it has evolved over time
        * TEAMMEMBER can show an example of this API in use in a way that is clear and readible
        * **Franklin** will explain how a UseCase uses this API -- animations are triggered by the backend changing the costume of a sprite.

`LanguageService.java`, `UIServiceProvider`
* TEAMMEMBER will show a Frontend API
    * **David** will explain how this API is able to accessed in a way that is open for extension and how it can be used and how it has evolved over time
        * TEAMMEMBER can show an example of this API in use in a way that is clear and readible
        * **David** will explain how a UseCase uses this API

#### Highlighting Design Choices:
* **Matthew** will explain a design element that has remained relatively stable during the project
    * `InputSourceConsumer` + composite pattern (To keep the use of the HumanInputManager stable)
* **Matthew?  or SOMEONE** will discuss a design element that has changed multiple times during the project
    * Controller codes (Demo/JSON/Regular)
    * For these changes, discuss how changes were brought up and how design decisions were approved


### Teamwork and Learning Moments
#### Project Management
* TEAMMEMBER will show the original wireframe and **George** discuss how things changed from the inital plans and the inital priorities
    * Each team-member will share what they have learned from the Agile method
        * Marc: The ability to work in parallel is incredibly powerful, but can be difficult to manage if communication breaks down or interfaces are poorly defined. All in all, it's a good tool for certain use-cases, OOP being one of them, but is by no means a *panacea*.
        * David: constructive feedback is productive; however, absent constructive feedback, presentations don't feel particularly helpful.
        * Franklin: I found that test-driven development helped to increase my productivity by creating a series of well-defined milestones and positive feedback.
        * Matt: Breaking things into sprints and being able to adapt to changes is conducive to flexilbity within team dynamics and the code base
        * George: I found that the constant updates supported by Agile were very helpful to my workflow.  The entire group was able to frequently identify blockers, ask for help, and this greatly improved morale and improved the efficiency of the entire team.

#### Timeline and Communication
* TEAMMEMBER will show a constructed timeline of events from this project
    * **Matthew** will discuss how communcation was handeled for each key event
    * Each team-member will share what they have learned from managing a project
        * Marc: Five people is about the point where it becomes untenable to host large meetings with all team-members so the implementation of sub-teams is necessitated. Done right, this is incredibly powerful.
        * David: large teams should focus on cross-sub-team dependencies first, in order to keep interfaces well-defined and avoid code rewrites during integration.
        * Franklin: Having frequent pair programming sessions helped to reinforce our understanding of parts of the codebase that we were less familiar with, and so helped keep everyone up to date on progress.
        * Matt: Constant communciation is neccessary to ensure that everyone is on the same page and working well together
        * George: for tackling large milestones that required extensive communication from many team members, large standing Zoom rooms are very helpful for active debugging.  To ensure these events can happen, good team responsiveness has been a blessing.

#### Improvment
* **Marc** will discuss how as a team we worked on improving the project
    * Each team-member will share something they learned about building a positive team culture
        * Marc: Communication and mutual respect is key. Try to keep tempers down and remember the human!
        * David: communicating availability early is key to being able to hold whole-team meetings, which is important, since whole-team meetings are necessary to keep everyone on the same page and feeling like they're contributing meaningfully.
        * Franklin: Frequent meetings helped to reinforce team camaraderie, and the establishment (and completion) of small goals gave us frequent positive feedback that kept us motivated.
        * Matt: Flexability is key! Being able to be adaptable and open to compromise helps keep a positive enviornment
        * George: Building a positive team culture also means relaxing and talking about things other than the project.  This includes other interests and the occasional breaks greatly helped improve team dynamics.

#### Team Contract
* TEAMMEMBER will show the original team contract
    * **David** will discuss what parts of the contract were used and what parts need revisions
    * Each team-member will share how communciation and collective problem-solving helped mitigate team issues
        * Marc: All in all, the team was communicative which helped a great deal in making sure that components of the project were completed on time and to specification. Maintaining an active set of issues on the GitLab was also important in keeping tabs on who was doing what and keeping accountability high.
        * David: early communication about defining interfaces and APIs helped avoid the sorts of intra-team conflicts that I'd experienced in both cell society and slogo.
        * Franklin: Putting team priorities over personal egos was critical to making sure that progress was consistent.
        * Matt: The standing zoom rooms really helped with communcations since were able to discuss things over media other than slack messages which I think helped prevent misunderstandings and hash out design ideas
        * George: I found that being willing to be flexible and transparent in the chat was helpful to coordinate extra meetings or to ask for help.  I found that this ensured necessary design changes or other blockers could be handled ahead of time, reducing stress.

### Conclusion
* Each member can discuss their final thoughts about this project?