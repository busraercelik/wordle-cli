package b.s.wordle.rules;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;

public class WordleGameExactMatchRule implements WordleGameRule{
    @Override
    public GuessResult evaluateGuess(GuessRequest guessRequest) {
        return null;
    }
}
