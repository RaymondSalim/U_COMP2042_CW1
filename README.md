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