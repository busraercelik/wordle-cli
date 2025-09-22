package b.s.wordle.view;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GameStatus;
import b.s.wordle.enums.SelectedOption;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ConsoleGameViewImpl implements GameView {

    // ANSI color codes
    private static final String CONSOLE_COLOR_RESET = "\u001B[0m";
    private static final String CONSOLE_COLOR_GREEN = "\u001B[42;30m";  // black text on green bg
    private static final String CONSOLE_COLOR_YELLOW = "\u001B[43;30m";  // black text on yellow bg
    private static final String CONSOLE_COLOR_GRAY = "\u001B[100;37m"; // white text on gray bg

    private final boolean debugMode;
    private final Terminal terminal;
    private final LineReader reader;
    private final PrintWriter writer;

    public ConsoleGameViewImpl(boolean debugMode) throws IOException {
        this.terminal = TerminalBuilder.builder().build();
        this.writer = this.terminal.writer();
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
        this.debugMode = debugMode;
    }

    @Override
    public void showMenuOptions() {
        clearView();
        writer.println("""
                Designed & developed by Busra Ercelik (bsr.ercelik@gmail.com)!
                
                Welcome to wordle.
                Choose from the options below.
                
                1. Show tutorial.
                2. Start new game.
                3. Exit.
                """);
        writer.flush();
    }

    @Override
    public void showGameExitMessage() {
        writer.println("Bye Bye!");
        writer.flush();
    }

    @Override
    public void showNewGameStartedMessage() {
        writer.write("""
                You can input 'X' and press enter to give up!
                
                New word selected.. Take a guess!
                """);
        writer.flush();
    }

    @Override
    public void showInvalidInputMessage(String helpText) {
        writer.println("Invalid input : "+helpText);
        writer.flush();
    }

    @Override
    public SelectedOption readMenuSelection() {
        try {
            String selectionString = reader.readLine(">> ");
            int selectedInt = Integer.parseInt(selectionString);
            return Arrays.stream(SelectedOption.values()).filter(s-> s.selection==selectedInt)
                    .findFirst().orElse(SelectedOption.BAD_SELECTION);
        } catch (Exception e){
            return SelectedOption.BAD_SELECTION;
        }
    }

    @Override
    public void printDebugInfo(String debugInfo) {
        if(!this.debugMode) return;
        writer.println("[DEBUG] "+debugInfo);
        writer.flush();
    }

    @Override
    public void clearView() {
        terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
        terminal.flush();
    }

    @Override
    public void showTutorial() {
        writer.write("""
            How to Play Wordle
            Your goal is to guess the hidden 5-letter word. You have 5 guesses to find it.

            Rules:
            Type in any valid 5-letter word as your guess.
            After you submit a guess, the letters will be colored to give you clues:
            
            """);

        writer.printf("    %s S %s → Correct letter, correct spot.%n", CONSOLE_COLOR_GREEN, CONSOLE_COLOR_RESET);
        writer.printf("    %s T %s → Correct letter, correct spot.%n", CONSOLE_COLOR_GREEN, CONSOLE_COLOR_RESET);
        writer.printf("    %s R %s → Letter is in the word, but in a different position.%n", CONSOLE_COLOR_YELLOW, CONSOLE_COLOR_RESET);
        writer.printf("    %s A %s → Letter is not in the word.%n", CONSOLE_COLOR_GRAY, CONSOLE_COLOR_RESET);
        writer.printf("    %s W %s → Letter is not in the word.%n", CONSOLE_COLOR_GRAY, CONSOLE_COLOR_RESET);

        writer.write("""

            Example:
            If the hidden word is STONE and you guessed STRAW:

            """);

        writer.printf("    %s S %s %s T %s %s R %s %s A %s %s W %s%n",
                CONSOLE_COLOR_GREEN, CONSOLE_COLOR_RESET, CONSOLE_COLOR_GREEN, CONSOLE_COLOR_RESET, CONSOLE_COLOR_YELLOW, CONSOLE_COLOR_RESET, CONSOLE_COLOR_GRAY, CONSOLE_COLOR_RESET, CONSOLE_COLOR_GRAY, CONSOLE_COLOR_RESET);

        writer.write("""

            Keep guessing until you solve the word or run out of attempts.
            """);
        writer.flush();
    }

    @Override
    public String readInput() {
        return reader.readLine(">> ");
    }

    @Override
    public void writeGuessResult(GuessResult guessResult) {
        List<GuessCharacter> guessedWord = guessResult.guessedWord();

        // Build the colored guess row
        StringBuilder sb = new StringBuilder("\n");
        for (GuessCharacter gc : guessedWord) {
            String color;
            switch (gc.color()) {
                case GREEN  -> color = CONSOLE_COLOR_GREEN;
                case YELLOW -> color = CONSOLE_COLOR_YELLOW;
                case NONE -> color = CONSOLE_COLOR_GRAY;
                default     -> color = CONSOLE_COLOR_RESET;
            }

            sb.append(color)
                    .append(" ")
                    .append(Character.toUpperCase(gc.ch()))
                    .append(" ")
                    .append(CONSOLE_COLOR_RESET)
                    .append(" ");
        }

        WordleGameState wordleGameState = guessResult.gameState();
        writer.println(sb + " attempts left: " + wordleGameState.getAttemptsRemaining());

        GameStatus gameStatus = wordleGameState.getGameStatus();
        // Then print final messages if game ended
        if (gameStatus == GameStatus.WON) {
            writer.println("Congratulations! You guessed the word!");
            writer.println("Press enter to go back to main menu.");
        } else if (gameStatus == GameStatus.LOST) {
            writer.println("Game over! You ran out of attempts.");
            // reveal the hidden word if GuessResult carries it
            writer.println("The word was: " + wordleGameState.getHiddenWord());
            writer.println("Press enter to go back to main menu.");
        }
        writer.flush();
    }

}
