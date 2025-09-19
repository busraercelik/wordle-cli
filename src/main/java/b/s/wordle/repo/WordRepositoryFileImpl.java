package b.s.wordle.repo;

import java.util.List;

public class WordRepositoryFileImpl implements WordRepository{
    @Override
    public List<String> getAllWords() {
        return List.of();
    }

    @Override
    public boolean existsIgnoreCase(String word) {
        return false;
    }
}
