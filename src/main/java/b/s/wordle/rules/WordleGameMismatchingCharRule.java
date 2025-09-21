package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;

import java.util.*;

import static b.s.wordle.enums.GuessColor.GREY;
import static b.s.wordle.enums.GuessColor.YELLOW;

public class WordleGameMismatchingCharRule implements WordleGameRule {

    @Override
    public List<GuessCharacter> evaluateGuess(GuessRequest guessRequest, String hiddenWord) {
        List<GuessCharacter> guessCharacters = new ArrayList<>();
        Map<Character, List<Integer>> hiddenCharIndicesMap = new HashMap<>();

        for (int i=0; i<hiddenWord.length(); i++) {
            // key -> character, values -> List of indices where these chars appear
            hiddenCharIndicesMap.computeIfAbsent(hiddenWord.charAt(i), index -> new ArrayList<>()).add(i);
        }

        for (int i=0; i<hiddenWord.length(); i++) {
            char guessChar = guessRequest.guess().charAt(i);

            if (hiddenCharIndicesMap.containsKey(Character.toUpperCase(guessChar))) {
                List<Integer> indices = hiddenCharIndicesMap.get(guessChar);
                // if any indices is same then it should be GREY, if no indice match then YELLOW.
                guessCharacters.add(new GuessCharacter(guessChar, indices.contains(i) ? GREY : YELLOW));
            } else {
                guessCharacters.add(new GuessCharacter(guessChar, GREY));
            }
        }
        return guessCharacters;
    }
}
