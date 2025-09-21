package b.s.wordle.repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WordRepositoryFileImpl implements WordRepository{

    private final List<String> words;

    public WordRepositoryFileImpl(String filePath) {
        words = loadFromFile(filePath);
    }

    @Override
    public List<String> getAllWords() {
        return words;
    }

    @Override
    public boolean existsIgnoreCase(String word) {
        return words.contains(word.toUpperCase().trim());
    }

    List<String> loadFromFile(String filePath) {
        try {
            return Files.readAllLines(
                    Path.of(filePath)).stream().map(String::trim).map(String::toUpperCase).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
