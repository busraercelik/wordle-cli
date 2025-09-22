package b.s.wordle.rules;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.enums.GuessColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static b.s.wordle.enums.GuessColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordleGameRuleImplTest {

    WordleGameRule wordleGameRule = new WordleGameRuleImpl();

    @CsvSource({
            // All green - exact match
            "GUAVA, GUAVA, GREEN-GREEN-GREEN-GREEN-GREEN",
            // All grey - no common letters
            "FUDGE, SPORT, NONE-NONE-NONE-NONE-NONE",
            // Mixed green and grey - partial exact matches
            "GUAVA, DRAMA, NONE-NONE-GREEN-NONE-GREEN",
            // Yellow - letters in different positions
            "SEOUL, CAIRO, NONE-NONE-NONE-NONE-YELLOW",
            // Mixed green, yellow, and grey
            "TOOTH, MOTIF, NONE-GREEN-YELLOW-NONE-NONE",
            // Duplicate 'E' in guess but not in hidden - PEEVE vs APPLE
            "APPLE, PEEVE, YELLOW-NONE-NONE-NONE-GREEN",
            // Duplicate 'E' in hidden but not in guess - GRADE vs GEESE
            "GEESE, GRADE, GREEN-NONE-NONE-NONE-GREEN",
            // Duplicate in both with mixed matches - ERASE vs SPEED
            "SPEED, ERASE, YELLOW-NONE-NONE-YELLOW-YELLOW",
    })
    @ParameterizedTest(name = "Hidden: {0}, Guess: {1}, Expected: {2}")
    void evaluateGuess_withVariousScenarios_returnsCorrectColors(String hiddenWord, String guessWord, String expectedColors) {
        GuessRequest guessRequest = new GuessRequest(guessWord);

        // When
        List<GuessCharacter> guessCharacters = wordleGameRule.evaluateGuess(guessRequest, hiddenWord);
        // Then
        String[] expectedColorsArray = expectedColors.split("-");
        assertEquals(expectedColorsArray.length, guessCharacters.size(),
                String.format("Expected %d characters but got %d", expectedColorsArray.length, guessCharacters.size()));

        // common assertions
        for (int index = 0; index < expectedColorsArray.length; index++) {
            GuessColor expectedColor = parseColor(expectedColorsArray[index]);
            assertEquals(expectedColor, guessCharacters.get(index).color(),
                    String.format("Position %d: Expected %s but got %s for letter '%c'",
                            index, expectedColor, guessCharacters.get(index).color(), guessCharacters.get(index).ch()));

            assertEquals(guessWord.toUpperCase().charAt(index), guessCharacters.get(index).ch(),
                    String.format("Position %d: Expected character '%c' but got '%c'",
                            index, guessWord.toUpperCase().charAt(index), guessCharacters.get(index).ch()));
        }
    }

    private GuessColor parseColor(String colorName) {
        return switch (colorName.trim().toUpperCase()) {
            case "GREEN" -> GREEN;
            case "YELLOW" -> YELLOW;
            case "NONE" -> NONE;
            default -> throw new IllegalArgumentException("Invalid color name: " + colorName +"!");
        };
    }
}