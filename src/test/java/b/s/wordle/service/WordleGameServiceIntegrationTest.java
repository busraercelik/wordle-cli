package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.enums.GuessColor;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordRepositoryFileImpl;
import b.s.wordle.repo.WordleGameStateRepository;
import b.s.wordle.repo.WordleGameStateRepositoryInMemoryImpl;
import b.s.wordle.rules.WordleGameRuleImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static b.s.wordle.enums.GameStatus.*;
import static b.s.wordle.enums.GuessColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WordleGameServiceImpl Integration Tests")
class WordleGameServiceIntegrationTest {

    // Mocking Random to control the hidden word selection for predictable tests
    @Mock
    Random randomGenerator;

    // Real implementations for integration testing
    WordRepository wordRepository;
    WordleGameService wordleGameService;
    WordleGameStateRepository wordleGameStateRepository;

    Path tempWordFile;

    @BeforeEach
    void setup() throws IOException {
        // 1. Create a temporary file with a known set of words
        tempWordFile = Files.createTempFile("words", ".txt");
        List<String> words = List.of("TRACE", "SMILE", "APPLE", "GRIND");
        Files.write(tempWordFile, words);

        // 2. Instantiate real dependencies
        wordRepository = new WordRepositoryFileImpl(tempWordFile.toAbsolutePath().toString());
        wordleGameStateRepository = new WordleGameStateRepositoryInMemoryImpl();

        // 3. Instantiate the service with a mix of real and mocked dependencies
        wordleGameService = new WordleGameServiceImpl(
                wordRepository,
                randomGenerator,
                wordleGameStateRepository,
                new WordleGameRuleImpl()
        );
    }

    @AfterEach
    void cleanup() throws IOException {
        // Clean up the temporary file after each test
        Files.deleteIfExists(tempWordFile);
    }

    @Test
    @DisplayName("Full Game Scenario: Player wins on the third attempt")
    void fullGame_shouldAllowPlayerToWin() {
        // --- Step 1: Start a new game ---
        // Given a list of words, we make the random generator pick "TRACE" (index 0)
        when(randomGenerator.nextInt(4)).thenReturn(0);
        wordleGameService.createNewGame();

        // --- Step 2: First Guess (Partially Correct) ---
        GuessRequest firstGuess = new GuessRequest("GRIND");
        GuessResult firstResult = wordleGameService.evaluateGuess(firstGuess);

        // Then: Assert game state is IN_PROGRESS
        assertNotNull(firstResult);
        assertEquals(IN_PROGRESS, firstResult.gameState().getGameStatus());
        assertEquals(4, firstResult.gameState().getAttemptsRemaining());
        // Assert colors: G(NONE) R(GREEN) I(NONE) N(NONE) D(NONE)
        // Hidden word:   T       R        A       C       E
        assertGuessResultColors(firstResult, List.of(NONE, GREEN, NONE, NONE, NONE));

        // --- Step 3: Second Guess (More Correct) ---
        GuessRequest secondGuess = new GuessRequest("CREAM");
        GuessResult secondResult = wordleGameService.evaluateGuess(secondGuess);

        // Then: Assert game state is still IN_PROGRESS
        assertEquals(IN_PROGRESS, secondResult.gameState().getGameStatus());
        assertEquals(3, secondResult.gameState().getAttemptsRemaining());
        // Assert colors: C(YELLOW) R(GREEN) E(YELLOW) A(YELLOW) M(NONE)
        // Hidden word:   T         R        A        C         E
        assertGuessResultColors(secondResult, List.of(YELLOW, GREEN, YELLOW, YELLOW, NONE));

        // --- Step 4: Third Guess (Correct) ---
        GuessRequest finalGuess = new GuessRequest("TRACE");
        GuessResult finalResult = wordleGameService.evaluateGuess(finalGuess);

        // Then: Assert game is WON
        assertEquals(WON, finalResult.gameState().getGameStatus());
        assertEquals(2, finalResult.gameState().getAttemptsRemaining());
        // Assert all colors are GREEN
        assertGuessResultColors(finalResult, List.of(GREEN, GREEN, GREEN, GREEN, GREEN));
    }

    @Test
    @DisplayName("Full Game Scenario: Player loses after running out of attempts")
    void fullGame_shouldResultInLossAfterFiveIncorrectGuesses() {
        // --- Step 1: Start a new game ---
        // Given a list of words, we make the random generator pick "SMILE" (index 1)
        when(randomGenerator.nextInt(4)).thenReturn(1);
        wordleGameService.createNewGame();

        // --- Step 2: Make 4 incorrect guesses ---
        wordleGameService.evaluateGuess(new GuessRequest("GRIND")); // Att: 4
        wordleGameService.evaluateGuess(new GuessRequest("TRACE")); // Att: 3
        wordleGameService.evaluateGuess(new GuessRequest("APPLE")); // Att: 2
        wordleGameService.evaluateGuess(new GuessRequest("WRONG")); // Att: 1

        // --- Step 3: Make the final incorrect guess ---
        GuessRequest finalGuess = new GuessRequest("QUEST");
        GuessResult finalResult = wordleGameService.evaluateGuess(finalGuess);

        // Then: Assert game is LOST
        assertEquals(LOST, finalResult.gameState().getGameStatus());
        assertEquals(0, finalResult.gameState().getAttemptsRemaining());
        // Assert hidden word is revealed correctly in the final state
        assertEquals("SMILE", finalResult.gameState().getHiddenWord());
        // Assert colors for the final guess: Q(NONE) U(NONE) E(YELLOW) S(YELLOW) T(NONE)
        assertGuessResultColors(finalResult, List.of(NONE, NONE, YELLOW, YELLOW, NONE));
    }

    @Test
    @DisplayName("Edge Case: Should handle duplicate letters correctly")
    void evaluateGuess_withDuplicateLetters_shouldProduceCorrectColors() {
        // --- Step 1: Start a new game with a word containing duplicates ---
        // We make the random generator pick "APPLE" (index 2)
        when(randomGenerator.nextInt(4)).thenReturn(2);
        wordleGameService.createNewGame();

        // --- Step 2: Guess a word with duplicates that tests the logic ---
        // Hidden: APPLE, Guess: PEEVE
        GuessRequest guess = new GuessRequest("PEEVE");
        GuessResult result = wordleGameService.evaluateGuess(guess);

        // Then: Assert the game state and colors
        assertEquals(IN_PROGRESS, result.gameState().getGameStatus());
        assertEquals(4, result.gameState().getAttemptsRemaining());
        
        // Assert colors: P(YELLOW) E(YELLOW) E(NONE) V(NONE) E(GREEN)
        // - First 'P' is YELLOW because it matches the second 'P' in APPLE.
        // - First 'E' is NONE because the single 'E' in APPLE is already accounted for by the exact match at the end.
        // - Second 'E' is NONE because the single 'E' in APPLE is already accounted for by the exact match at the end.
        // - 'V' is NONE.
        // - Third 'E' is GREEN because it's an exact match.
        assertGuessResultColors(result, List.of(YELLOW, NONE, NONE, NONE, GREEN));
    }

    /**
     * Helper method to assert the colors of a guess result.
     * @param result The GuessResult from the service.
     * @param expectedColors A list of expected GuessColor enums in order.
     */
    private void assertGuessResultColors(GuessResult result, List<GuessColor> expectedColors) {
        List<GuessCharacter> guessedWord = result.guessedWord();
        assertEquals(expectedColors.size(), guessedWord.size(), "Mismatch in word length");
        for (int i = 0; i < expectedColors.size(); i++) {
            assertEquals(expectedColors.get(i), guessedWord.get(i).color(),
                    "Incorrect color for character at index " + i);
        }
    }
}