package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;

import java.util.ArrayList;
import java.util.List;

import static b.s.wordle.enums.GuessColor.GREEN;
import static b.s.wordle.enums.GuessColor.GREY;

public class WordleGameExactMatchRule implements WordleGameRule{

    @Override
    public List<GuessCharacter> evaluateGuess(GuessRequest guessRequest, String hiddenWord) {
        int guessWordLen = guessRequest.guess().length();
        List<GuessCharacter> characters = new ArrayList<>();

        for (int i=0; i<guessWordLen; i++) {
            characters.add(new GuessCharacter(guessRequest.guess().charAt(i),
                    guessRequest.guess().charAt(i) == hiddenWord.charAt(i) ? GREEN : GREY));
        }
        
        return characters;
    }
}
