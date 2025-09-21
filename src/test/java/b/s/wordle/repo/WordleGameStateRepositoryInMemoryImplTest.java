package b.s.wordle.repo;

import b.s.wordle.dto.WordleGameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static b.s.wordle.enums.GameStatus.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class WordleGameStateRepositoryInMemoryImplTest {

    WordleGameStateRepositoryInMemoryImpl repositoryInMemory;

    @BeforeEach
    void setup(){
        repositoryInMemory = new WordleGameStateRepositoryInMemoryImpl();
    }

    @Test
    void verifySaveGameState() {
        WordleGameState wordleGameState = new WordleGameState("MANGO", 5);
        WordleGameState updatedWordleGameState = repositoryInMemory.save(wordleGameState);
        assertEquals(wordleGameState, updatedWordleGameState);
    }

    @Test
    void verifyGetWordleGameState() {
        WordleGameState initialGameState = new WordleGameState("MANGO", 5);
        repositoryInMemory.save(initialGameState);
        WordleGameState fetchedGameState = repositoryInMemory.get();
        assertEquals(initialGameState, fetchedGameState);
    }

    @Test
    void verifyInitUpdateAndGetState() {
        WordleGameState initialGameState = repositoryInMemory.save(new WordleGameState("MANGO", 5));
        WordleGameState fetchedGameState = repositoryInMemory.get();
        fetchedGameState.setGameStatus(WON);
        fetchedGameState.setAttemptsRemaining(2);

        WordleGameState updatedGameState = repositoryInMemory.save(fetchedGameState);

        assertNotEquals(initialGameState, updatedGameState);

        assertEquals(WON, updatedGameState.getGameStatus());
        assertEquals("MANGO", updatedGameState.getHiddenWord());
        assertEquals(2, updatedGameState.getAttemptsRemaining());
    }
}