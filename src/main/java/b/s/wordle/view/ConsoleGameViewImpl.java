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
    private static final String CLEAR_SCREEN = "\u001B[2J";
    private static final String CURSOR_HOME  = "\u001B[H";
    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[42;30m";  // black text on green bg
    private static final String YELLOW = "\u001B[43;30m";  // black text on yellow bg
    private static final String GRAY   = "\u001B[100;37m"; // white text on gray bg

    private final Terminal terminal;
    private final LineReader reader;
    private final PrintWriter writer;

    public ConsoleGameViewImpl() throws IOException {
        this.terminal = TerminalBuilder.builder().build();
        this.writer = this.terminal.writer();
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    @Override
    public void showMenuOptions() {
        writer.println("""
                Designed & developed by Busra Ercelik (bsr.ercelik@gmail.com)!
                
                Welcome to wordle.
                Choose from the options below.
                
                1. Show tutorial.
                2. Start new game.
                3. Exit.
                """);
        writer.write(">>");
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
    public void showBadMenuSelectionMessage() {
        writer.write("Please enter a valid option!");
        writer.write(">>");
        writer.flush();
    }

    @Override
    public SelectedOption readMenuSelection() {
        try {
            String selectionString = reader.readLine();
            int selectedInt = Integer.parseInt(selectionString);
            return Arrays.stream(SelectedOption.values()).filter(s-> s.selection==selectedInt)
                    .findFirst().orElse(SelectedOption.BAD_SELECTION);
        } catch (Exception e){
            return SelectedOption.BAD_SELECTION;
        }
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

        writer.printf("    %s S %s → Correct letter, correct spot.%n", GREEN, RESET);
        writer.printf("    %s T %s → Correct letter, correct spot.%n", GREEN, RESET);
        writer.printf("    %s R %s → Letter is in the word, but in a different position.%n", YELLOW, RESET);
        writer.printf("    %s A %s → Letter is not in the word.%n", GRAY, RESET);
        writer.printf("    %s W %s → Letter is not in the word.%n", GRAY, RESET);

        writer.write("""

            Example:
            If the hidden word is STONE and you guessed STRAW:

            """);

        writer.printf("    %s S %s %s T %s %s R %s %s A %s %s W %s%n",
                GREEN, RESET, GREEN, RESET, YELLOW, RESET, GRAY, RESET, GRAY, RESET);

        writer.write("""

            Keep guessing until you solve the word—or run out of attempts.
            """);

        writer.write(">>");
        writer.flush();
    }

    @Override
    public String readGuess() {
        return reader.readLine();
    }

    @Override
    public void writeGuessResult(GuessResult guessResult) {
        List<GuessCharacter> guessedWord = guessResult.guessedWord();

        // Build the colored guess row
        StringBuilder sb = new StringBuilder("\n");
        for (GuessCharacter gc : guessedWord) {
            String color;
            switch (gc.color()) {
                case GREEN -> color = GREEN;
                case YELLOW -> color = YELLOW;
                case GREY   -> color = GRAY;
                default     -> color = RESET;
            }

            sb.append(color)
                    .append(" ")
                    .append(Character.toUpperCase(gc.ch()))
                    .append(" ")
                    .append(RESET)
                    .append(" ");
        }

        WordleGameState wordleGameState = guessResult.gameStatus();
        writer.println(sb + " attempts left: " + wordleGameState.getAttemptsRemaining());

        GameStatus gameStatus = wordleGameState.getGameStatus();
        // Then print final messages if game ended
        if (gameStatus == GameStatus.WON) {
            writer.println("Congratulations! You guessed the word!");
        } else if (gameStatus == GameStatus.LOST) {
            writer.println("Game over! You ran out of attempts.");
            // reveal the hidden word if GuessResult carries it
            String hiddenWord = guessedWord.stream()
                    .map(gc -> String.valueOf(Character.toUpperCase(gc.ch())))
                    .reduce("", String::concat);
            writer.println("The word was: " + hiddenWord);
        }

        writer.write(">>");
        writer.flush();
    }

}
