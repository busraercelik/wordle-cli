package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;

import java.util.List;

public class WordleGameMismatchingCharRule implements WordleGameRule{

    @Override
    public List<GuessCharacter> evaluateGuess(GuessRequest guessRequest, String hiddenWord) {
        return null;
    }
}
