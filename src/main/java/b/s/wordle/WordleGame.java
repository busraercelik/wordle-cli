package b.s.wordle;

import b.s.wordle.view.ConsoleGameView;
import b.s.wordle.view.GameView;

import java.io.IOException;

public class WordleGame {

    private final GameView gameView;

    public WordleGame(GameView gameView) {
        this.gameView = gameView;
    }

    public static void main(String[] args) throws IOException {
        GameView gameView = new ConsoleGameView();
        WordleGame wordleGame = new WordleGame(gameView);
        wordleGame.startGame();
    }

    void startGame() {

    }

}