package b.s.wordle.constant;

import java.util.Set;

public final class GameConstants {
    private GameConstants(){}

    public static final String MODE_ARG_NAME = "--mode";
    public static final String WORD_FILE_ARG_NAME = "--word-file";
    public static final String DEBUG_MODE = "debug";
    public static final String DEFAULT_WORD_FILE_NAME = "words.txt";
    public static final String DEFAULT_MODE = "normal";
    public static final Set<String> DEFAULT_WORDS = Set.of(
            "Milan", "Paris", "Tokyo", "Dubai", "Seoul", "Miami", "Tampa", "Izmir", "Bursa", "Cairo");

    public static final String GIVE_UP = "x";
    public static final char CONSUMED = '\0';
}
