package b.s.wordle.repo;

import java.util.List;

public interface WordRepository {
    List<String> getAllWords();
    boolean existsIgnoreCase(String word);
}
