package b.s.wordle.dto;

import b.s.wordle.enums.GameStatus;

import java.util.Objects;

public class WordleGameState {
    private int attemptsRemaining;
    private GameStatus gameStatus;
    private final String hiddenWord;

    public WordleGameState(WordleGameState state){
        this.gameStatus = state.gameStatus;
        this.hiddenWord = state.hiddenWord;
        this.attemptsRemaining = state.attemptsRemaining;
    }

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

    @Override
    public String toString() {
        return "WordleGameState{" +
                "attemptsRemaining=" + attemptsRemaining +
                ", gameState=" + gameStatus +
                ", hiddenWord='" + hiddenWord + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WordleGameState that)) return false;
        return attemptsRemaining == that.attemptsRemaining && gameStatus == that.gameStatus && Objects.equals(hiddenWord, that.hiddenWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attemptsRemaining, gameStatus, hiddenWord);
    }
}
