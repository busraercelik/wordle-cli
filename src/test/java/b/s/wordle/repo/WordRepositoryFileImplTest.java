package b.s.wordle.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

class WordRepositoryFileImplTest {

    Path tempFile;
    WordRepository wordRepository;

    @BeforeEach
    void setup() throws IOException {
        tempFile = Files.createTempFile("words", "txt");
        Files.writeString(tempFile, """
                water
                otter
                haste
                hasty
                angry
                robot
                """);
    }

    @Test
    void getAllWords() {
        wordRepository = new WordRepositoryFileImpl(tempFile.toAbsolutePath().toString());
        Assertions.assertEquals(
                Set.of("WATER", "OTTER", "HASTE", "HASTY", "ANGRY", "ROBOT"),
                wordRepository.getAllWords(), "Words did not match!"
        );
    }

    @Test
    void existsIgnoreCase() {
        wordRepository = new WordRepositoryFileImpl(tempFile.toAbsolutePath().toString());
        Assertions.assertTrue(wordRepository.existsIgnoreCase("water"), "Word not found!");
    }

}