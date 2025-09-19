package b.s.wordle.rules;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;

public interface WordleGameRule {
    GuessResult evaluateGuess(GuessRequest guessRequest);
}
