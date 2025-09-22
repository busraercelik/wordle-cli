# wordle-cli
A wordle mini game made in core java showing MVC and clean code practices.

## How to Play
You have to guess the hidden word in 5 tries to win the game. 
Each guess must be a valid 5-letter word.
After each guess, the color of the letters will change to show how close your guess was to the word.
- **Green**: The letter is in the word and in the correct spot.
- **Yellow**: The letter is in the word but in the wrong spot.
- **Gray**: The letter is not in the word in any spot.

To give up, type `x` and press Enter. This will take you back to the main menu.

## How to Run
To run the application, you can use the `play.sh` script:
```bash
chmod +x build.sh
chmod +x play.sh
./build.sh
./play.sh
```
To run the application in debug mode, you can use the following command:
```bash
./play.sh --mode debug --word-file words.txt
```
This will print the hidden word at the beginning of the game. 
You can use a custom word list by using the `--word-file` argument. The application will create a default file
with preset words if there is no such file present. By default the application looks for a file `words.txt` in the directory from which the game is launched.

## How it Works
The application is a simple command-line Wordle game built with Java. It follows a Model-View-Controller (MVC) architecture:
- **Model**: The `dto`, `enums`, `service` and `repo` packages represent the model. They contain the data structures and business logic of the game.
- **View**: The `view` package contains the `ConsoleGameViewImpl` class, which is responsible for displaying the game state to the user and reading user input.
- **Controller**: The `controller` package contains the `GameController` class, which handles user input and updates the model and view accordingly.

The main entry point of the application is the `WordleGame` class. It initializes the game by creating the necessary objects and then shows the main menu. The game loop is handled by the `GameController`, which reads user input and calls the appropriate methods in the `WordleGameService` to update the game state. The `WordleGameService` contains the core game logic, such as creating a new game, evaluating a guess, and checking if the game is over.


## Screenshots
### Main Menu
<img width="522" height="212" alt="image" src="https://github.com/user-attachments/assets/9b541f16-f8a0-426b-a4d9-9a8c3b843e17" />

### Tutorial
<img width="625" height="400" alt="image" src="https://github.com/user-attachments/assets/913ad9dc-2c2c-42eb-aeee-80c8d9a40e1a" />

### Game
<img width="609" height="448" alt="image" src="https://github.com/user-attachments/assets/96a30549-12a5-4e98-b9e3-eb8e0d70561b" />


