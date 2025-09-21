package b.s.wordle;

import b.s.wordle.controller.GameController;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordRepositoryFileImpl;
import b.s.wordle.repo.WordleGameStateRepositoryInMemoryImpl;
import b.s.wordle.rules.WordleGameExactMatchRule;
import b.s.wordle.rules.WordleGameMismatchingCharRule;
import b.s.wordle.service.WordleGameServiceImpl;
import b.s.wordle.service.WordleGameService;
import b.s.wordle.util.ArgUtils;
import b.s.wordle.view.ConsoleGameViewImpl;
import b.s.wordle.view.GameView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static b.s.wordle.constant.GameConstants.*;

public class WordleGame {

    public static void main(String[] args) throws IOException {
        String mode = ArgUtils.readArg(args, MODE_ARG_NAME, DEFAULT_MODE);
        String wordFile = ArgUtils.readArg(args, WORD_FILE_ARG_NAME, DEFAULT_WORD_FILE_NAME);
        createDefaultWordsIfNotExists(wordFile);
        GameView gameView = new ConsoleGameViewImpl(mode.equalsIgnoreCase(DEBUG_MODE));
        WordRepository wordRepository = new WordRepositoryFileImpl(wordFile);

        WordleGameService wordleGameService = new WordleGameServiceImpl(wordRepository,
                List.of(new WordleGameExactMatchRule(), new WordleGameMismatchingCharRule()),
                new WordleGameStateRepositoryInMemoryImpl()
        );

        GameController wordleGameController = new GameController(gameView, wordleGameService);
        wordleGameController.showMenu();
    }

    private static void createDefaultWordsIfNotExists(String wordFile) throws IOException {
        Path file = Path.of(wordFile);
        if(Files.exists(file)) return;
        String words = DEFAULT_WORDS.stream().collect(Collectors.joining(System.lineSeparator()));
        Files.writeString(file, words);
    }

}