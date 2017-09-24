# Game-Engine
A Simple Game Engine coded in Java using only native libraries

# Project Idea
This project started out as a way to help teach others Java through coding a game. It provides the user the ability to create a simple 2D, top down game quickly. I also wanted to learn more about game development so the only libraries used were native to Java (ie Java2D).

# Demo
Examples for creating a simple game are included under src/example

The below demo is running at ~120 FPS for a little over 100 game objects (10 x 10 grouping of water objects). The game has been tested to run at roughly the same FPS with over 1000 game objects (Game objects include trees, players, water blocks, etc)
![Demo](demo/demo1.gif)

# Usage
Setup just requires including this project into your code base, no external libraries / dependencies are required.

# Current Capabilities
- Player and object creation
- Camera following an object
- Easy IO handling for keyboard (mouse support currently not implemented)
- Fast drawing (Only draws visible objects to improve game speed)
- Animations (Allows for a game object to "move" through images at different speeds) (Used to the spider's movement and the water)

# TODO
- Implement a sound manager
- Expand demo to include a platformer game
- Ability to easily import and export levels into the game
- General optimizations for game loop
