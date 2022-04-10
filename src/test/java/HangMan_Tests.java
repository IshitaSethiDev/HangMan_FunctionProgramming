import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HangMan_Tests {
    HangMan testObj = new HangMan();
    List<Character> playerGuesses = new ArrayList<>();
    List<Character> wordList = new ArrayList<>();
    String word = "hi";
    String letterGuess = "i";

    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("Firing up unit tests");
    }

    @BeforeEach
    void nextTest() {
        System.out.println("Running Next Unit test........");
    }

    @DisplayName("Checking for Duplicates: True")
    @Test
    void getPlayerGuessTrue() {
        playerGuesses.add('i');
        assertEquals(true,
                testObj.getPlayerGuess( playerGuesses, letterGuess),
                "Test Failed for Player Guess One");
    }

    @DisplayName("Checking for Duplicates: False")
    @Test
    void getPlayerGuessFalse() {
        playerGuesses.add('h');
        assertEquals(false,
                testObj.getPlayerGuess( playerGuesses, letterGuess),
                "Test Failed for Player Guess Two");
    }

    @DisplayName("Checking if the user guessed the word correctly")
    @Test
    void printWordStateTrue() {
        playerGuesses.add('h');
        playerGuesses.add('i');
        assertEquals(true,
                testObj.printWordState(word, playerGuesses),
                "Test Failed for Print word State True");
    }

    @DisplayName("Checking if the user didn't guess the word correctly as of yet")
    @Test
    void printWordStateFalse() {
        playerGuesses.add('h');
        assertEquals(false,
                testObj.printWordState(word, playerGuesses),
                "Test Failed for Print word State False");
    }

    @DisplayName("Checking if the user guessed the correct letter")
    @Test
    void correctGuessTrue() {
        wordList.add('h');
        wordList.add('i');
        assertEquals(true,
                testObj.correctGuess(wordList, letterGuess),
                "Test Failed for Correct Guess Letter True");
    }

    @DisplayName("Checking if the user didn't guess the correct letter")
    @Test
    void correctGuessFalse() {
        wordList.add('a');
        wordList.add('a');
        assertEquals(false,
                testObj.correctGuess(wordList, letterGuess),
                "Test Failed for Correct Guess Letter False");
    }

    @DisplayName("Checking if the user has exhausted his 6 wrong tries")
    @Test
    void wrongCountTrue() {
        assertEquals(true,
                testObj.wrongCount(6),
                "Test Failed for Wrong Count True");
    }

    @DisplayName("Checking if the user has not exhausted his 6 wrong tries")
    @Test
    void wrongCountFalse() {
        assertEquals(false,
                testObj.wrongCount(4),
                "Test Failed for Wrong Count False");
    }

    @AfterEach
    void afterTest() {
        System.out.println("This test completed");
    }

    @AfterAll
    public static void afterAllTests() {
        System.out.println("All tests are completed.");
    }

}
