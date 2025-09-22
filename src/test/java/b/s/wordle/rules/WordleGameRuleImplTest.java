package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static b.s.wordle.enums.GuessColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleGameRuleImplTest {

    WordleGameRule wordleGameRule = new WordleGameRuleImpl();


    @Test
    void evaluateGuess_withAllMatchingGuessWord_returnsAllCharactersGreen() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("GUAVA");
        List<GuessCharacter> guessCharacters =
                wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertTrue(guessCharacters.stream().allMatch(gchar -> gchar.color() == GREEN));
    }

    @Test
    void evaluateGuess_withPartialMatchingGuessWord_returnsCharactersInGreenAndGrey() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("DRAMA");
        List<GuessCharacter> guessCharacters =
                wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertEquals(2, guessCharacters.stream().filter(gchar -> gchar.color()==GREEN).count());
        assertEquals(3, guessCharacters.stream().filter(gchar -> gchar.color()== NONE).count());
    }

    @Test
    void evaluateGuess_withNoExactMatchingGuessWord_returnsAllLettersInGrey() {
        // Given
        String hiddenWord = "GUAVA";
        GuessRequest guessRequest = new GuessRequest("MANGO");
        List<GuessCharacter> guessCharacters =
                wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        // Then
        assertEquals(5, guessCharacters.stream().filter(gchar -> gchar.color()== NONE).count());
    }

    @Test
    void evaluateGuess_withNoMatch_returnsCharactersInGrey() {
        GuessRequest guessRequest = new GuessRequest("MANGO");
        String hiddenWord = "ELITE";
        List<GuessCharacter> guessCharacters = wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        assertTrue(guessCharacters.stream().allMatch(guessCharacter -> guessCharacter.color() == NONE));
    }

    @Test
    void evaluateGuess_withMatchingCharsInDifferentPositions_returnsCharactersInYellow() {
        GuessRequest guessRequest = new GuessRequest("CAIRO");
        String hiddenWord = "SEOUL"; // 'A' and 'M' are common chars
        List<GuessCharacter> guessCharacters = wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        assertEquals(NONE, guessCharacters.get(0).color());
        assertEquals('C', guessCharacters.get(0).ch());

        assertEquals(NONE, guessCharacters.get(1).color()); //A
        assertEquals('A', guessCharacters.get(1).ch());

        assertEquals(NONE, guessCharacters.get(2).color()); //N
        assertEquals('I', guessCharacters.get(2).ch());

        assertEquals(NONE, guessCharacters.get(3).color()); //G
        assertEquals('R', guessCharacters.get(3).ch());

        assertEquals(YELLOW, guessCharacters.get(4).color()); //O
        assertEquals('O', guessCharacters.get(4).ch());
    }


    @Test
    void evaluateGuess_withMatchingCharsInSameAndDifferentPositions_returnsCharactersNotYellow() {
        GuessRequest guessRequest = new GuessRequest("MOTIF");
        String hiddenWord = "TOOTH"; // 'O' should be GREY color, 'T' should be YELLOW color
        List<GuessCharacter> guessCharacters = wordleGameRule.evaluateGuess(guessRequest, hiddenWord);

        assertEquals(NONE, guessCharacters.get(0).color()); //M
        assertEquals('M', guessCharacters.get(0).ch());

        assertEquals(NONE, guessCharacters.get(1).color()); //A
        assertEquals('O', guessCharacters.get(1).ch());

        assertEquals(YELLOW, guessCharacters.get(2).color()); //N
        assertEquals('T', guessCharacters.get(2).ch());

        assertEquals(NONE, guessCharacters.get(3).color()); //G
        assertEquals('I', guessCharacters.get(3).ch());

        assertEquals(NONE, guessCharacters.get(4).color()); //O
        assertEquals('F', guessCharacters.get(4).ch());
    }
}