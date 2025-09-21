package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.enums.GuessColor;
import b.s.wordle.repo.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static b.s.wordle.enums.GameStatus.WON;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WordleGameServiceImplTest {

    @InjectMocks
    WordleGameServiceImpl wordleGameService;
    @Mock
    WordRepository wordRepository;

    @Test
    void evaluateGuess_withMatchingGuessWord_returnsCharactersInOnlyGreenColor() {
        // Given
        String hiddenWord = "MANGO";
        GuessRequest guessRequest = new GuessRequest("MANGO");
        List<GuessCharacter> mockGuessCharacters = new ArrayList<>();

        for (char hiddenChar : hiddenWord.toCharArray()) {
            mockGuessCharacters.add(new GuessCharacter(hiddenChar, GuessColor.GREEN));
        }
        // When
        GuessResult guessResult = wordleGameService.evaluateGuess(guessRequest);
        // Then
        assertEquals(WON, guessResult.gameStatus().getGameStatus());
        assertEquals(mockGuessCharacters, guessResult.guessedWord());
    }
}