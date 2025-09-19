package b.s.wordle;

import b.s.wordle.controller.GameController;
import b.s.wordle.view.ConsoleGameView;
import b.s.wordle.view.GameView;

import java.io.IOException;

public class WordleGame {

    public static void main(String[] args) throws IOException {
        GameView gameView = new ConsoleGameView();
        GameController wordleGameController = new GameController(gameView);
        wordleGameController.showMenu();
    }

}