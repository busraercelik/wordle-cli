package b.s.wordle;

import b.s.wordle.controller.GameController;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordRepositoryFileImpl;
import b.s.wordle.rules.WordleGameExactMatchRule;
import b.s.wordle.rules.WordleGameMismatchingCharRule;
import b.s.wordle.service.WordleGameServiceImpl;
import b.s.wordle.service.WordleGameService;
import b.s.wordle.view.ConsoleGameViewImpl;
import b.s.wordle.view.GameView;

import java.io.IOException;
import java.util.List;

public class WordleGame {

    public static void main(String[] args) throws IOException {
        GameView gameView = new ConsoleGameViewImpl(args[0].equalsIgnoreCase("debug"));
        WordRepository wordRepository = new WordRepositoryFileImpl();

        WordleGameService wordleGameService = new WordleGameServiceImpl(
                wordRepository, List.of(new WordleGameExactMatchRule(), new WordleGameMismatchingCharRule()));

        GameController wordleGameController = new GameController(gameView, wordleGameService);
        wordleGameController.showMenu();
    }

}