package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GuessColor;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordleGameStateRepository;
import b.s.wordle.rules.WordleGameRuleImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static b.s.wordle.enums.GameStatus.*;
import static b.s.wordle.enums.GuessColor.NONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordleGameServiceImplTest {

    @Mock
    Random randomGenerator;
    @Mock
    WordRepository wordRepository;
    @Mock
    WordleGameRuleImpl worldGameRule;
    @Mock
    WordleGameStateRepository wordleGameStateRepository;

    @Captor
    ArgumentCaptor<WordleGameState> wordleGameStateArgumentCaptor;

    WordleGameService wordleGameService;

    @BeforeEach
    void setup() {
        wordleGameService = new WordleGameServiceImpl(
                wordRepository, randomGenerator, wordleGameStateRepository, worldGameRule);
    }

    @Test
    void createNewGame() {
        List<String> citiesWordle = List.of("BURSA", "IZMIR", "PARIS");
        when(wordRepository.getAllWords()).thenReturn(citiesWordle);
        when(randomGenerator.nextInt(citiesWordle.size())).thenReturn(1); //select IZMIR
        WordleGameState expectedGameState = new WordleGameState("IZMIR", 5);
        when(wordleGameStateRepository.save(
                wordleGameStateArgumentCaptor.capture())).thenReturn(expectedGameState);

        wordleGameService.createNewGame();
        // state created to save
        WordleGameState createdState = wordleGameStateArgumentCaptor.getValue();
        assertEquals(expectedGameState, createdState);
    }

    @Test
    void testLostGuessEvaluation(){
        // Given
        String hiddenWord = "MANGO";
        GuessRequest guessRequest = new GuessRequest("IZMIR");
        WordleGameState wordleGameState = new WordleGameState(hiddenWord, 1);
        WordleGameState updatedWordleGameState = new WordleGameState(hiddenWord, 0);
        updatedWordleGameState.setGameStatus(LOST);

        // When
        when(wordleGameStateRepository.get()).thenReturn(wordleGameState);
        when(worldGameRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(getGuessCharacters(hiddenWord, NONE));
        when(wordleGameStateRepository.save(updatedWordleGameState)).thenReturn(updatedWordleGameState);
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);

        // Then
        assertEquals(LOST, guessResult.gameState().getGameStatus());
        assertEquals(getGuessCharacters(hiddenWord, NONE), guessResult.guessedWord());
        assertEquals(0, guessResult.gameState().getAttemptsRemaining());
    }

    @Test
    void testWinningGuessEvaluation() {
        // Given
        String hiddenWord = "MANGO";
        GuessRequest guessRequest = new GuessRequest("MANGO");
        List<GuessCharacter> allGreenGuessChars = getGuessCharacters(hiddenWord, GuessColor.GREEN);
        WordleGameState wordleGameState = new WordleGameState(hiddenWord, 5);
        WordleGameState updatedWordleGameState = new WordleGameState(hiddenWord, 4);
        updatedWordleGameState.setGameStatus(WON);

        // When
        when(wordleGameStateRepository.get()).thenReturn(wordleGameState);
        when(worldGameRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(allGreenGuessChars);
        when(wordleGameStateRepository.save(updatedWordleGameState)).thenReturn(updatedWordleGameState);
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);

        // Then
        assertEquals(WON, guessResult.gameState().getGameStatus());
        assertEquals(allGreenGuessChars, guessResult.guessedWord());
    }

    @Test
    void testInProgressGuessEvaluation() {
        // Given
        String hiddenWord = "WATER";
        GuessRequest guessRequest = new GuessRequest("OTTER");
        List<GuessCharacter> expectedResults = List.of(
                new GuessCharacter('O', NONE),
                new GuessCharacter('T', NONE),
                new GuessCharacter('T', GuessColor.GREEN),
                new GuessCharacter('E', GuessColor.GREEN),
                new GuessCharacter('R', GuessColor.GREEN)
        );

        WordleGameState wordleGameState = new WordleGameState(hiddenWord, 5);
        WordleGameState updatedWordleGameState = new WordleGameState(hiddenWord, 4);

        // When
        when(wordleGameStateRepository.get()).thenReturn(wordleGameState);
        when(worldGameRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(expectedResults);
        when(wordleGameStateRepository.save(updatedWordleGameState)).thenReturn(updatedWordleGameState);
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);

        // Then
        assertEquals(IN_PROGRESS, guessResult.gameState().getGameStatus());
        assertEquals(expectedResults, guessResult.guessedWord());
    }

    private static List<GuessCharacter> getGuessCharacters(String hiddenWord, GuessColor guessColor) {
        List<GuessCharacter> mockGuessCharacters = new ArrayList<>();
        for (char hiddenChar : hiddenWord.toCharArray()) {
            mockGuessCharacters.add(new GuessCharacter(hiddenChar, guessColor));
        }
        return mockGuessCharacters;
    }

}