package b.s.wordle.view;

import b.s.wordle.dto.GuessResult;

public interface GameView {
    void showTutorial();
    String readGuess();
    void writeGuessResult(GuessResult guessResult);
}
