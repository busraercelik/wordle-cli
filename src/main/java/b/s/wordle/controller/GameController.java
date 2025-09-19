package b.s.wordle.controller;

import b.s.wordle.view.GameView;

public class GameController {

    private final GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
    }

    public void showMenu() {
        gameView.showMenuOptions();
        while(true){
             switch (gameView.readMenuSelection()){
                 case START_NEW_GAME -> startGame();
                 case SHOW_TUTORIAL -> gameView.showTutorial();
                 case EXIT -> {
                     gameView.showGameExitMessage();
                     System.exit(0);
                 }
                 case BAD_SELECTION -> gameView.showBadMenuSelectionMessage();
             }
        }
    }

    public void startGame() {

    }
}
