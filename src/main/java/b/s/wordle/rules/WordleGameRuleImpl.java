package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;

import java.util.*;

import static b.s.wordle.constant.GameConstants.CONSUMED;
import static b.s.wordle.enums.GuessColor.*;

public class WordleGameRuleImpl implements WordleGameRule {

    @Override
    public List<GuessCharacter> evaluateGuess(GuessRequest guessRequest, String hiddenWord) {

        List<Character> guessWordCharList = guessRequest.guess()
                .chars().mapToObj(ch-> (char) Character.toUpperCase(ch)).toList();
        List<Character> hiddenWordCharList = new ArrayList<>(
                hiddenWord.chars().mapToObj(ch -> (char) Character.toUpperCase(ch)).toList());

        List<GuessCharacter> guessCharacters = new ArrayList<>();

        // Initialize colored guess word
        for (Character character : guessWordCharList) {
            guessCharacters.add(new GuessCharacter(character, NONE));
        }

        // Mark exact matches (GREEN) and consume characters
        for(int index = 0; index < guessWordCharList.size(); index++){
            char currentGuessChar = guessWordCharList.get(index);
            // same char present in hidden word at the same position - exact match
            if(hiddenWordCharList.get(index).equals(currentGuessChar)){
                guessCharacters.set(index, new GuessCharacter(currentGuessChar, GREEN));
                hiddenWordCharList.set(index, CONSUMED); // mark consumed
            }
        }

        // Once green are processed, mark partial matches (YELLOW) from remaining characters
        for(int index = 0; index < guessWordCharList.size(); index++){
            char currentGuessChar = guessWordCharList.get(index);

            // Skip if already marked as GREEN
            if(guessCharacters.get(index).color() == GREEN) {
                continue;
            }

            // Check if char is present in remaining unconsumed characters
            if(hiddenWordCharList.contains(currentGuessChar)){
                guessCharacters.set(index, new GuessCharacter(currentGuessChar, YELLOW));
                // Find and consume the first occurrence of this character
                int indexOfMismatchingChar = hiddenWordCharList.indexOf(currentGuessChar);
                hiddenWordCharList.set(indexOfMismatchingChar, CONSUMED);
            }
            // If not present at all, it stays as NONE (already set in initialization)
        }

        return guessCharacters;
    }
}