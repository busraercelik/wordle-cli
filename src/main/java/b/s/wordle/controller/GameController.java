package b.s.wordle.controller;

import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GameStatus;
import b.s.wordle.service.WordleGameService;
import b.s.wordle.view.GameView;

import static b.s.wordle.constant.GameConstants.GIVE_UP;

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
                 case BAD_SELECTION -> gameView.showInvalidInputMessage("enter \"1\" to show tutorial, \"2\" to start a new game, \"3\" to quit");

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

        GameStatus status = GameStatus.IN_PROGRESS;
        while(true){
            String input = gameView.readInput();
            if(status!= GameStatus.IN_PROGRESS) {
                gameView.showMenuOptions();
                return;
            }

            //empty input
            if(input.trim().isEmpty()) continue;

            //give up!
            if(input.equalsIgnoreCase(GIVE_UP)) {
                gameView.showMenuOptions();
                return;
            }
            //input of invalid size
            if(input.trim().length()!=5) {
                gameView.showInvalidInputMessage("enter a word 5 letter word or \"X\" to give up");
                continue;
            }

            //valid game input
            GuessResult guessResult = wordleGameService.evaluateGuess(new GuessRequest(input));
            gameView.writeGuessResult(guessResult);
            status = guessResult.gameState().getGameStatus();
        }
    }
}
