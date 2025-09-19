package b.s.wordle.service;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;

public interface WordleGameService {
    WordleGameState createNewGame();
    GuessResult evaluateGuess(GuessRequest guessRequest);
}
