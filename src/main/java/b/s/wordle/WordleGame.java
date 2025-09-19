package b.s.wordle;

import b.s.wordle.controller.GameController;
import b.s.wordle.dto.WordleGameServiceImpl;
import b.s.wordle.service.WordleGameService;
import b.s.wordle.view.ConsoleGameViewImpl;
import b.s.wordle.view.GameView;

import java.io.IOException;

public class WordleGame {

    public static void main(String[] args) throws IOException {
        GameView gameView = new ConsoleGameViewImpl();
        WordleGameService wordleGameService = new WordleGameServiceImpl();
        GameController wordleGameController = new GameController(gameView, wordleGameService);
        wordleGameController.showMenu();
    }

}