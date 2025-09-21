package b.s.wordle.repo;

import b.s.wordle.dto.WordleGameState;

public interface WordleGameStateRepository {
    WordleGameState get();
    WordleGameState save(WordleGameState wordleGameState);
}
