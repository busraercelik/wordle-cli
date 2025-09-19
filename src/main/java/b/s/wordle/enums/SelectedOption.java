package b.s.wordle.enums;

public enum SelectedOption {
    SHOW_TUTORIAL(1),
    START_NEW_GAME(2),
    EXIT(3),
    BAD_SELECTION(-1);

    public final int selection;

    SelectedOption(int selection) {
        this.selection = selection;
    }
}
