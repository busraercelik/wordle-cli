package b.s.wordle.dto;

import b.s.wordle.enums.GameStatus;

public class WordleGameState {
    private int attemptsRemaining;
    private GameStatus gameStatus;
    private final String hiddenWord;

    public WordleGameState(String hiddenWord, int maxAttempts) {
        this.hiddenWord = hiddenWord;
        this.attemptsRemaining = maxAttempts;
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public void setAttemptsRemaining(int attemptsRemaining) {
        this.attemptsRemaining = attemptsRemaining;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
