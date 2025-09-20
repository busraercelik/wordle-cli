package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static b.s.wordle.enums.GuessColor.GREEN;
import static b.s.wordle.enums.GuessColor.GREY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleGameExactMatchRuleTest {

    WordleGameExactMatchRule wordleGameExactMatchRule = new WordleGameExactMatchRule();

    @Test
    void evaluateGuess_withAllMatchingGuessWord_returnsAllCharactersGreen() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("GUAVA");
        List<GuessCharacter> guessCharacters =
                wordleGameExactMatchRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertTrue(guessCharacters.stream().allMatch(gchar -> gchar.color() == GREEN));
    }

    @Test
    void evaluateGuess_withPartialMatchingGuessWord_returnsCharactersInGreenAndGrey() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("DRAMA");
        List<GuessCharacter> guessCharacters =
                wordleGameExactMatchRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertEquals(2, guessCharacters.stream().filter(gchar -> gchar.color()==GREEN).count());
        assertEquals(3, guessCharacters.stream().filter(gchar -> gchar.color()==GREY).count());
    }

    @Test
    void evaluateGuess_withNoExactMatchingGuessWord_returnsAllLettersInGrey() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("MANGO");
        List<GuessCharacter> guessCharacters =
                wordleGameExactMatchRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertEquals(5, guessCharacters.stream().filter(gchar -> gchar.color()==GREY).count());
    }
}