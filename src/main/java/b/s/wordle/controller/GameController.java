package b.s.wordle.controller;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.service.WordleGameService;
import b.s.wordle.view.GameView;

public class GameController {

    private final GameView gameView;
    private final WordleGameService wordleGameService;

    public GameController(GameView gameView, WordleGameService wordleGameService) {
        this.gameView = gameView;
        this.wordleGameService = wordleGameService;
    }

    public void showMenu() {
        gameView.showMenuOptions();
        while(true){
             switch (gameView.readMenuSelection()){
                 case START_NEW_GAME -> startGame();
                 case SHOW_TUTORIAL -> gameView.showTutorial();
                 case BAD_SELECTION -> gameView.showBadMenuSelectionMessage();

                 case EXIT -> {
                     gameView.showGameExitMessage();
                     System.exit(0);
                 }
             }
        }
    }

    public void startGame() {
        WordleGameState newGame = wordleGameService.createNewGame();
        gameView.printDebugInfo("hidden word -> "+ newGame.getHiddenWord());
        gameView.showNewGameStartedMessage();
        while(true){
            String input = gameView.readGuess();
            //empty input
            if(input.trim().isEmpty()) continue;

            //give up!
            if(input.equalsIgnoreCase("x")) {
                gameView.clearView();
                gameView.showMenuOptions();
                return;
            }

            //valid game input
            GuessResult guessResult = wordleGameService.evaluateGuess(new GuessRequest(input));
            gameView.writeGuessResult(guessResult);
        }
    }
}
