package b.s.wordle.dto;

import b.s.wordle.enums.GameState;

import java.util.List;

public record GuessResult(GameState gameState, int guessesLeft, List<GuessCharacter> guessedWord) {
}
