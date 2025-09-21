package b.s.wordle.repo;

import b.s.wordle.dto.WordleGameState;

public class WordleGameStateRepositoryInMemoryImpl implements WordleGameStateRepository{

    private WordleGameState wordleGameState;

    @Override
    public WordleGameState get() {
        return new WordleGameState(wordleGameState);
    }

    @Override
    public WordleGameState save(WordleGameState wordleGameState) {
        this.wordleGameState = new WordleGameState(wordleGameState);
        return this.wordleGameState;
    }
}
