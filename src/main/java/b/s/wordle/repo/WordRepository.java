package b.s.wordle.repo;

import java.util.Set;

public interface WordRepository {
    Set<String> getAllWords();
    boolean existsIgnoreCase(String word);
}
