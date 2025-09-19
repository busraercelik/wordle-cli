package b.s.wordle.service;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.rules.WordleGameRule;

import java.util.List;

public class WordleGameServiceImpl implements WordleGameService {

    private final WordRepository wordRepository;
    private final List<WordleGameRule> wordleGameRules;

    public WordleGameServiceImpl(WordRepository wordRepository, List<WordleGameRule> wordleGameRules) {
        this.wordRepository = wordRepository;
        this.wordleGameRules = wordleGameRules;
    }

    @Override
    public WordleGameState createNewGame() {
        return null;
    }

    @Override
    public GuessResult evaluateGuess(GuessRequest guessRequest) {
        return null;
    }
}
