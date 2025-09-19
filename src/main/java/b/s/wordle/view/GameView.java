package b.s.wordle.view;

import b.s.wordle.dto.GuessResult;
import b.s.wordle.enums.SelectedOption;

public interface GameView {
    void printDebugInfo(String debugInfo);

    void clearView();
    void showTutorial();
    void showMenuOptions();
    void showGameExitMessage();
    void showNewGameStartedMessage();
    void showBadMenuSelectionMessage();

    String readGuess();
    SelectedOption readMenuSelection();

    void writeGuessResult(GuessResult guessResult);
}
