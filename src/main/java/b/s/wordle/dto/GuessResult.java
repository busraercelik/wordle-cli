package b.s.wordle.dto;

import java.util.List;

public record GuessResult(WordleGameState gameStatus, List<GuessCharacter> guessedWord) {
}
