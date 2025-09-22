package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GameStatus;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordleGameStateRepository;
import b.s.wordle.rules.WordleGameRule;

import java.util.List;
import java.util.random.RandomGenerator;

import static b.s.wordle.enums.GuessColor.GREEN;

public class WordleGameServiceImpl implements WordleGameService {

    private final WordleGameRule wordleGameRule;
    private final WordRepository wordRepository;
    private final RandomGenerator randomGenerator;
    private final WordleGameStateRepository wordleGameStateRepository;

    public WordleGameServiceImpl(
            WordRepository wordRepository, RandomGenerator randomGenerator,
            WordleGameStateRepository wordleGameStateRepository, WordleGameRule wordleGameRule) {
        this.wordleGameRule = wordleGameRule;
        this.wordRepository = wordRepository;
        this.randomGenerator = randomGenerator;
        this.wordleGameStateRepository = wordleGameStateRepository;
    }

    @Override
    public WordleGameState createNewGame() {
        List<String> allWords = wordRepository.getAllWords();
        String hiddenWord = allWords.get(randomGenerator.nextInt(allWords.size()));
        return wordleGameStateRepository.save(new WordleGameState(hiddenWord,5));
    }

    @Override
    public GuessResult evaluateGuess(GuessRequest guessRequest) {
        GuessResult guessResult;
        WordleGameState wordleGameState = wordleGameStateRepository.get();
        String hiddenWord = wordleGameState.getHiddenWord();

        List<GuessCharacter> mergedCharList = wordleGameRule.evaluateGuess(guessRequest, hiddenWord);
        int attemptsRemaining = wordleGameState.getAttemptsRemaining() - 1;

        // scenario 1 : guessed the hidden word - WON
        if (mergedCharList.stream().allMatch(guessCharacter ->  guessCharacter.color() == GREEN)) {
            wordleGameState.setGameStatus(GameStatus.WON);
        }
        // scenario 2 : not guessed the hidden word -  IN_PROGRESS || LOST
        else if (attemptsRemaining > 0) {
            wordleGameState.setGameStatus(GameStatus.IN_PROGRESS);
        } else {
            wordleGameState.setGameStatus(GameStatus.LOST);
        }
        wordleGameState.setAttemptsRemaining(attemptsRemaining);
        wordleGameStateRepository.save(wordleGameState);
        guessResult = new GuessResult(wordleGameState, mergedCharList);
        return guessResult;
    }

}
