package b.s.wordle.service;

import b.s.wordle.dto.GuessCharacter;
import b.s.wordle.dto.GuessRequest;
import b.s.wordle.dto.GuessResult;
import b.s.wordle.dto.WordleGameState;
import b.s.wordle.enums.GameStatus;
import b.s.wordle.enums.GuessColor;
import b.s.wordle.repo.WordRepository;
import b.s.wordle.repo.WordleGameStateRepository;
import b.s.wordle.rules.WordleGameRule;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

import static b.s.wordle.enums.GuessColor.*;

public class WordleGameServiceImpl implements WordleGameService {

    private final WordRepository wordRepository;
    private final RandomGenerator randomGenerator;
    private final List<WordleGameRule> wordleGameRules;
    private final WordleGameStateRepository wordleGameStateRepository;

    public WordleGameServiceImpl(
            WordRepository wordRepository, RandomGenerator randomGenerator,
            WordleGameStateRepository wordleGameStateRepository, List<WordleGameRule> wordleGameRules) {
        this.wordRepository = wordRepository;
        this.randomGenerator = randomGenerator;
        this.wordleGameRules = wordleGameRules;
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

        // merge the rule set results to get colored guessed word
        List<GuessCharacter> mergedCharList = mergeRuleSetResults(guessRequest, hiddenWord);
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

    private List<GuessCharacter> mergeRuleSetResults(GuessRequest guessRequest, String hiddenWord) {
        return wordleGameRules.stream()
                .map(rule -> rule.evaluateGuess(guessRequest, hiddenWord))
                .reduce((charList1, charList2)-> {
                    List<GuessCharacter> merged = new ArrayList<>();
                    for (int i=0; i<charList1.size(); i++) {
                        GuessCharacter guessCharacter1 = charList1.get(i);
                        GuessCharacter guessCharacter2 = charList2.get(i);
                        GuessColor finalColor = mergeColors(guessCharacter1, guessCharacter2);

                        merged.add(new GuessCharacter(guessCharacter1.ch(), finalColor));
                    }
                    return merged;
                }).orElseGet(ArrayList::new);
    }

    private static GuessColor mergeColors(GuessCharacter guessCharacter1, GuessCharacter guessCharacter2) {
        GuessColor color1 = guessCharacter1.color();
        GuessColor color2 = guessCharacter2.color();
        GuessColor finalColor;
        if (color1 == GREEN || color2 == GREEN) {
            finalColor = GREEN;
        } else if (color1 == YELLOW || color2 == YELLOW) {
            finalColor = YELLOW;
        } else {
            finalColor = GREY;
        }
        return finalColor;
    }
}
