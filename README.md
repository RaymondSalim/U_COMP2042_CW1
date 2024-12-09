# COMP2042 Coursework
Raymond Salim 20595118

[GitHub Repository](https://github.com/RaymondSalim/U_COMP2042_CW1)

# Compilation Instruction

## Prerequisites

First things first, ensure that the following applications are installed:

1. Maven (v3.9 or above)
2. Java (JDK 19 or above)

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

## Features
- Add pause menu in game
- Implement Observer Pattern to handle game events
- Game Window is resizable

## Improvements

- There are some test regarding audio ([here](src/test/java/com/example/demo/audio/AudioManagerTest.java)), that has
  been commented out due to intermittent errors, causing CI to fail.
    - Sometimes it takes less than 5 seconds for the test to pass, sometimes it might take minutes