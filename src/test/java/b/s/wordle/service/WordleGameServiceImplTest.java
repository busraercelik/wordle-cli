package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GuessColor;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordleGameStateRepository;
import b.s.wordle.rules.WordleGameExactMatchRule;
import b.s.wordle.rules.WordleGameMismatchingCharRule;
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
import static b.s.wordle.enums.GuessColor.GREY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordleGameServiceImplTest {

    @Mock
    Random randomGenerator;
    @Mock
    WordRepository wordRepository;
    @Mock
    WordleGameExactMatchRule exactRule;
    @Mock
    WordleGameMismatchingCharRule mismatchRule;
    @Mock
    WordleGameStateRepository wordleGameStateRepository;

    @Captor
    ArgumentCaptor<WordleGameState> wordleGameStateArgumentCaptor;

    WordleGameServiceImpl wordleGameService;

    @BeforeEach
    void setup() {
        wordleGameService = new WordleGameServiceImpl(wordRepository,
                randomGenerator, wordleGameStateRepository, List.of(exactRule, mismatchRule));
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
        when(exactRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(getGuessCharacters(hiddenWord, GREY));
        when(mismatchRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(getGuessCharacters(hiddenWord, GREY));
        when(wordleGameStateRepository.save(updatedWordleGameState)).thenReturn(updatedWordleGameState);
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);

        // Then
        assertEquals(LOST, guessResult.gameState().getGameStatus());
        assertEquals(getGuessCharacters(hiddenWord, GREY), guessResult.guessedWord());
        assertEquals(0, guessResult.gameState().getAttemptsRemaining());
    }

    @Test
    void testWinningGuessEvaluation() {
        // Given
        String hiddenWord = "MANGO";
        GuessRequest guessRequest = new GuessRequest("MANGO");
        List<GuessCharacter> allGreenGuessChars = getGuessCharacters(hiddenWord, GuessColor.GREEN);
        List<GuessCharacter> allGreyGuessChars = getGuessCharacters(hiddenWord, GREY);
        WordleGameState wordleGameState = new WordleGameState(hiddenWord, 5);
        WordleGameState updatedWordleGameState = new WordleGameState(hiddenWord, 4);
        updatedWordleGameState.setGameStatus(WON);

        // When
        when(wordleGameStateRepository.get()).thenReturn(wordleGameState);
        when(exactRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(allGreenGuessChars);
        when(mismatchRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(allGreyGuessChars);
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
        List<GuessCharacter> exactMathResults = List.of(
                new GuessCharacter('O', GREY),
                new GuessCharacter('T', GREY),
                new GuessCharacter('T', GuessColor.GREEN),
                new GuessCharacter('E', GuessColor.GREEN),
                new GuessCharacter('R', GuessColor.GREEN)
        );
        List<GuessCharacter> mismatchingResults = List.of(
                new GuessCharacter('O', GREY),
                new GuessCharacter('T', GREY),
                new GuessCharacter('T', GREY),
                new GuessCharacter('E', GREY),
                new GuessCharacter('R', GREY)
        );
        WordleGameState wordleGameState = new WordleGameState(hiddenWord, 5);
        WordleGameState updatedWordleGameState = new WordleGameState(hiddenWord, 4);

        // When
        when(wordleGameStateRepository.get()).thenReturn(wordleGameState);
        when(exactRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(exactMathResults);
        when(mismatchRule.evaluateGuess(guessRequest, hiddenWord)).thenReturn(mismatchingResults);
        when(wordleGameStateRepository.save(updatedWordleGameState)).thenReturn(updatedWordleGameState);
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);

        // Then
        assertEquals(IN_PROGRESS, guessResult.gameState().getGameStatus());
        assertEquals(exactMathResults, guessResult.guessedWord());
    }

    private static List<GuessCharacter> getGuessCharacters(String hiddenWord, GuessColor guessColor) {
        List<GuessCharacter> mockGuessCharacters = new ArrayList<>();
        for (char hiddenChar : hiddenWord.toCharArray()) {
            mockGuessCharacters.add(new GuessCharacter(hiddenChar, guessColor));
        }
        return mockGuessCharacters;
    }

}