package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static b.s.wordle.enums.GuessColor.GREY;
import static b.s.wordle.enums.GuessColor.YELLOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleGameMismatchingCharRuleTest {

    WordleGameMismatchingCharRule wordleGameMismatchingCharRule = new WordleGameMismatchingCharRule();

    @Test
    void evaluateGuess_withNoMatch_returnsCharactersInGrey() {
        GuessRequest guessRequest = new GuessRequest("MANGO");
        String hiddenWord = "ELITE";
        List<GuessCharacter> guessCharacters = wordleGameMismatchingCharRule.evaluateGuess(guessRequest, hiddenWord);

        assertTrue(guessCharacters.stream().allMatch(guessCharacter -> guessCharacter.color() == GREY));
    }

    @Test
    void evaluateGuess_withMatchingCharsInDifferentPositions_returnsCharactersInYellow() {
        GuessRequest guessRequest = new GuessRequest("MANGO");
        String hiddenWord = "AMBER"; // 'A' and 'M' are common chars
        List<GuessCharacter> guessCharacters = wordleGameMismatchingCharRule.evaluateGuess(guessRequest, hiddenWord);

        assertEquals(YELLOW, guessCharacters.get(0).color());
        assertEquals('M', guessCharacters.get(0).ch());

        assertEquals(YELLOW, guessCharacters.get(1).color()); //A
        assertEquals('A', guessCharacters.get(1).ch());

        assertEquals(GREY, guessCharacters.get(2).color()); //N
        assertEquals('N', guessCharacters.get(2).ch());

        assertEquals(GREY, guessCharacters.get(3).color()); //G
        assertEquals('G', guessCharacters.get(3).ch());

        assertEquals(GREY, guessCharacters.get(4).color()); //O
        assertEquals('O', guessCharacters.get(4).ch());
    }


    @Test
    void evaluateGuess_withMatchingCharsInSameAndDifferentPositions_returnsCharactersNotYellow() {
        GuessRequest guessRequest = new GuessRequest("MOTIF");
        String hiddenWord = "TOOTH"; // 'O' should be GREY color, 'T' should be YELLOW color
        List<GuessCharacter> guessCharacters = wordleGameMismatchingCharRule.evaluateGuess(guessRequest, hiddenWord);

        assertEquals(GREY, guessCharacters.get(0).color()); //M
        assertEquals('M', guessCharacters.get(0).ch());

        assertEquals(GREY, guessCharacters.get(1).color()); //A
        assertEquals('O', guessCharacters.get(1).ch());

        assertEquals(YELLOW, guessCharacters.get(2).color()); //N
        assertEquals('T', guessCharacters.get(2).ch());

        assertEquals(GREY, guessCharacters.get(3).color()); //G
        assertEquals('I', guessCharacters.get(3).ch());

        assertEquals(GREY, guessCharacters.get(4).color()); //O
        assertEquals('F', guessCharacters.get(4).ch());
    }
}