Flappy Bird Game
----------------

Overview
--------
This project is a Flappy Bird-style game developed using Java Swing. It features a responsive user interface, bird movement physics, dynamic obstacles, collision detection, and score tracking. The game includes start, in-game, and game-over states for a complete user experience.

Features
--------
- Bird Movement: Control the bird using the SPACE key to navigate through pipes.
- Obstacle Generation: Randomized pipes appear from the right and move left across the screen.
- Scoring System: Earn points each time the bird successfully passes through a set of pipes.
- Interactive Game Flow: Includes start screen, live score display, and game-over screen with options to restart or quit.

Requirements
------------
- Java 8 or higher
- JDK and JRE installed
- Any IDE (e.g., IntelliJ, Eclipse, NetBeans) or terminal to compile and run

How To Use
----------
- Save the Game File
  - Copy the provided Java code.
  - Save it into a file named: Flappy_Bird_Game.java

- Compile the Game
  - Open your terminal or command prompt.
  - Navigate to the directory where the file is saved.
  - Run the following command: javac Flappy_Bird_Game.java

- Run the Game
  - After successful compilation, run the game using: java Flappy_Bird_Game

Controls
--------
- ENTER – Start or restart the game
- SPACE – Make the bird flap upward
- ESC – Quit the game

How It Works
------------
- Initialization:
  - Sets up a JFrame window and uses JPanel for rendering.
  - Initializes bird, pipes, score, and game states.

- Gameplay Logic:
  - The bird is affected by gravity and flaps with upward velocity when SPACE is pressed.
  - Pipes move left across the screen and regenerate at set intervals with random heights.
  - The score increases by one each time the bird successfully passes through a pipe gap.

- Collision Detection:
  - The game ends if the bird hits the top/bottom boundaries or collides with a pipe.
  - A score screen is displayed for a brief moment after a collision.

- Restart and Exit:
  - Start: Press ENTER on the start screen to begin the game.
  - Restart: Press ENTER on the game over screen to play again.
  - Exit: Press ESC on the start screen or game over screen to quit the game.

Notes
-----
- Ensure that your system has a GUI environment to run Java Swing applications.
- All values such as gravity, pipe speed, and gap height can be tweaked in the code.
- No external libraries are used—only standard Java is required.
- Use a keyboard to play—mouse and touch are not supported.
