# COMP2042 Coursework
Raymond Salim 20595118

Project developed on

```shell
> uname -a
Darwin rsalim-MBP 23.5.0 Darwin Kernel Version 23.5.0: Wed May  1 20:12:58 PDT 2024; root:xnu-10063.121.3~5/RELEASE_ARM64_T6000 arm64
```

[GitHub Repository](https://github.com/RaymondSalim/U_COMP2042_CW1)

# Compilation Instruction

## Prerequisites

First things first, ensure that the following applications are installed:

1. Maven (v3.9 or above)
2. Java (JDK 19 or above) (OpenJDK 19 is used for development)

You can verify this by executing the following command

```shell
mvn --version
java --version
```

## Running
Run the following command to run the app
```shell
mvn clean javafx:run
```

## Javadoc
Run the following command to generate the Javadoc
```shell
mvn javadoc:javadoc
```

For convenience, Javadoc has been generated and is accessible by visiting
this [link](https://raymondsalim.github.io/U_COMP2042_CW1/)

# Game Assets

Most of the game assets are initially provided in the repository.

Additional game assets are obtained and edited based from the following links

- https://craftpix.net/freebies/free-space-shooter-game-gui/
- https://craftpix.net/freebies/free-sky-with-clouds-background-pixel-art-set/
- https://craftpix.net/freebies/free-space-shooter-game-objects/
- https://www.kenney.nl/assets/interface-sounds
- https://www.kenney.nl/assets/sci-fi-sounds
- https://onemansymphony.bandcamp.com/album/reflective-district-vol-2-free

The license for the additional assets can be found [here](https://craftpix.net/file-licenses/)
and [here](https://creativecommons.org/publicdomain/zero/1.0/).

# Changes
Here is the list of (hopefully) all the changes made

## Refactor
- Update folder structure to separate code to MVC structure
- Update code to use inheritance better (specifically Level Views and Models)
- Move timeline ownership to be in GameController
- Separated / create controllers

## Features (Working)
- GitHub Actions
  - Require maven build to pass before merging
  - Create release on merge to `main` branch. View Releases [here](https://github.com/RaymondSalim/U_COMP2042_CW1/releases)
  - Generate and deploy Javadoc on merge to `main` branch
    
- Add pause menu in game
- Add Game Over overlay
- Add FPS Cap
- Add Audio
- Add Audio Volume Control
- Implement Observer Pattern to handle game events
- Game Window is resizable
- Level Selection
- Scoring System
- Button animation (on hover)

## Improvements (Features Not Implemented)
- There are some test regarding audio ([here](src/test/java/com/example/demo/audio/AudioManagerTest.java)), that has
  been commented out due to intermittent errors, causing CI to fail.
    - Sometimes it takes less than 5 seconds for the test to pass, sometimes it might take minutes
- The idea of power-ups was considered, but due to the lack of time, was ultimately dropped.
- The idea of scene transition/animation was also considered, but dropped due to time constraints.

## Java Classes

```shell
.
├── Main.java [Modified] - Init Controllers, Set Context and Audio Manager
├── audio
│   ├── AudioEnum.java [New] - Enumerates the different audio types used in the application.
│   ├── AudioFiles.java [New] - Maps AudioEnum keys to their corresponding file paths.
│   ├── AudioManager.java [New] - A singleton class responsible for handling audio playback, including sound effects and background music.
├── context
│   ├── AppContext.java [New] - Manages global settings and properties such as screen dimensions, target frames per second (FPS), and audio volume.
├── controller
│   ├── GameController.java [New] - Manages the overall game flow and navigation between screens.
│   ├── LevelController.java [New] - Handles level-specific logic, game updates, and in-level overlays.
│   ├── UIController.java [New] - Manages user interface screens and overlays.
├── enums
│   ├── Direction.java [New] - Represents directions for movement.
│   ├── GameState.java [New] - Represents the various states of the game.
│   ├── LevelType.java [New] - Represents the types of levels available in the game.
├── factory
│   ├── LevelFactory.java [New] - Factory class for creating levels based on their enum type.
├── model
│   ├── base
│   │   └── LevelParent.java [Modified] - Code refactor
│   ├── LevelFour.java [New] - New model for new level
│   ├── LevelOne.java [Modified] - Make class more focused on Model\'s responsibility
│   ├── LevelThree.java [New] - New model for new level
│   ├── LevelTwo.java [Modified] - Make class more focused on Model\'s responsibility
│   ├── Player.java [New] - New model for Player\'s data (such as health, kill count)
├── observer
│   ├── base
│   │   └── BaseObservable.java [New] - A generic base class for implementing the observer pattern.
│   ├── GameStateObservable.java [New] - An observable class that notifies GameStateObservers about game state events.
│   ├── GameStateObserver.java [New] - An interface defining methods for responding to game state changes. 
├── utils
│   ├── Constants.java [New] - Contains shared constants used throughout the application.
│   ├── NavigationHandler.java [New] - An interface for managing navigation between screens.
└── view
    ├── components
    │   ├── HeartDisplay.java [Modified] - Displays `<3 x 5` instead of `<3<3<3<3<3`
    │   ├── ImageButton.java [New] - A button with hover and click effects.
    │   ├── ImageCheckbox.java [New] - A toggleable checkbox with visual feedback.
    │   ├── RollingBackground.java [New] - A scrolling background effect.
    ├── entities
    │   ├── ActiveActor.java [Modified]
    │   ├── ActiveActorDestructible.java [Modified]
    │   ├── Destructible.java [Old]
    │   ├── FighterPlane.java [Modified]
    │   ├── Projectile.java [Modified]
    ├── levels
    │   ├── LevelFour.java [New]
    │   ├── LevelOne.java [New]
    │   ├── LevelThree.java [New]
    │   ├── LevelTwo.java [New]
    │   ├── LevelView.java [New] - Abstract base class for creating level-specific visuals and UI.
    ├── objects
    │   ├── Boss.java [Modified]
    │   ├── BossProjectile.java [Modified]
    │   ├── EnemyPlane.java [Modified]
    │   ├── EnemyProjectile.java [Modified]
    │   ├── UserPlane.java [Modified]
    │   ├── UserProjectile.java [Modified]
    ├── overlays
    │   ├── GameOverOverlay.java [New] - Displayed when the game ends due to player loss.
    │   ├── LevelCompleteOverlay.java [New] - Shown upon completing a level, displaying score and stars.
    │   ├── Overlay.java [New] - Interface for common overlay behavior.
    │   ├── PauseMenuOverlay.java [New] - A menu overlay for pausing the game, adjusting settings, and resuming gameplay.
    └── screens
        ├── CreditsScreen.java [New] - Displays the credits for the game.
        ├── LevelSelectScreen.java [New] - Allows players to select a level to play.
        ├── MenuScreen.java [New] - The main menu screen, providing navigation to other game sections.
        ├── Screen.java [New] - Interface for common screen behavior.
        ├── SettingsScreen.java [New] - A screen for adjusting game settings like volume and FPS.
```

## Problems Encountered

- General Setup and Config
  - JUnit integration: Multiple issues encountered during integration, especially regarding to running the tests. One
    solution that solves an issue raises another issue...
- Game
  - Resizing Issue: Resizing the game mid-level causes the scrolling background to either overlap or produce gap. A
    solution to this is to reset the rolling background on resize. It *works*, but is not pretty.
  - Audio: Playing of audio can be problematic (especially when multiple short audio are played rapidly e.x. missile
    spam). A solution to this is to pool the MediaPlayers into a Queue.
  - Transfer of Data Between Deeply-nested Classes and Parent: Occasionally, deeply-nested child has to communicate some
    data with its parent or vice-versa. One solution to this is to use singleton (only when it makes sense), or use
    callbacks
  - Listener Overload: Initially, I used too much listeners to watch for changes. I solved this by using JavaFX's
    Properties, which allows binding value to another property.
