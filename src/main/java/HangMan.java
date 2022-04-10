import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class HangMan {
	public static void main(String[] args)  {
		HangMan myObj = new HangMan();
		Scanner keyboard = new Scanner(System.in);
		Scanner scanner = null;
		try {
			scanner = new Scanner(
					new URL("https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt").openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> words = new ArrayList<>();
		while (scanner.hasNext()) {
			words.add(scanner.next());
		}
		List<String> hangManDraw = myObj.getHangManDraw();
		String word;
		boolean flag = true;

		while (flag) {
			Random rand = new Random();
			word = words.get(rand.nextInt(words.size()));
			List<Character> playerGuesses = new ArrayList<>();
			List<Character> wordList = word.chars().mapToObj(obj -> (char) obj).collect(Collectors.toList());

			Integer wrongCount = 0;
			int score = 0;
			System.out.println("H A N G M A N");
			myObj.printHangedMan(wrongCount, hangManDraw);
			System.out.println("Please enter a letter:");
			start(keyboard, myObj, playerGuesses, wrongCount, word, wordList, hangManDraw, score, false);
			flag = myObj.playAgain(keyboard);
		}
	}

	public static void start(Scanner keyboard, HangMan myObj, List<Character> playerGuesses, Integer wrongCount,
			String word, List<Character> wordList, List<String> hangManDraw, int score, Boolean isLoseORWon)
			 {
		String letterGuess = keyboard.next().toLowerCase();
		boolean checkDuplicate = myObj.getPlayerGuess(playerGuesses, letterGuess);

		if (myObj.printWordState(word, playerGuesses)) {
			System.out.println("You win!");
			System.out.println("Please enter your name");
			String playerName = keyboard.next().toLowerCase();
			if (myObj.writePlayerDetailsIntoFile(playerName + "----" + score)) {
				System.out.println("You are the high scorer");
			}
			isLoseORWon = true;
		}
		if (!isLoseORWon) {
			if (checkDuplicate) {
				System.out.println("You already entered the letter. PLease try something else.");
				start(keyboard, myObj, playerGuesses, wrongCount, word, wordList, hangManDraw, score, isLoseORWon);
			} else if (myObj.correctGuess(wordList, letterGuess)) {
				System.out.println("That is a Correct guess...");
				score = score + 10;
				start(keyboard, myObj, playerGuesses, wrongCount, word, wordList, hangManDraw, score, isLoseORWon);
			} else {
				System.out.println("Nop not the correct guess...Try again..");
				wrongCount++;
				myObj.printHangedMan(wrongCount, hangManDraw);

				if (myObj.wrongCount(wrongCount)) {
					System.out.println("You lose! and Your score is " + score);
					System.out.println("The word was: " + word);
					System.out.println("Please enter your name");
					String playerName = keyboard.next().toLowerCase();
					if (myObj.writePlayerDetailsIntoFile(playerName + "----" + score) && score!=0) {
						System.out.println("You are the high scorer");
					}
				} else {
					start(keyboard, myObj, playerGuesses, wrongCount, word, wordList, hangManDraw, score, isLoseORWon);
				}
			}
		}
	}

	/**
	 * This function checks if the user guessed the correct letter.
	 * @param wordList
	 * @param letterGuess
	 * @return
	 */
	public boolean correctGuess(List<Character> wordList, String letterGuess) {
		return wordList.contains(letterGuess.charAt(0));
	}

	/**
	 * This function checks if the user has exhausted his 6 wrong tries...
	 * @param wrongCount
	 * @return
	 */
	public boolean wrongCount(Integer wrongCount) {
		if (wrongCount >= 6) {
			return true;
		}
		return false;
	}

	/**
	 * This function checks if the user has entered the value earlier returning it
	 * as duplicate...
	 * @param playerGuesses
	 * @param letterGuess
	 * @return
	 */
	public boolean getPlayerGuess(List<Character> playerGuesses, String letterGuess) {
		boolean checkDuplicate = playerGuesses.contains(letterGuess.charAt(0));
		playerGuesses.add(letterGuess.charAt(0));
		StringBuilder strBuild = new StringBuilder();
		for (Character guess : playerGuesses) {
			strBuild.append(guess);
		}
		System.out.println();
		System.out.println("Player guesses are: " + strBuild);
		return checkDuplicate;
	}

	/**
	 * If the player guessed the correct letter the letter is displayed otherwise
	 * (_) is displayed.
	 * @param word
	 * @param playerGuesses
	 * @return
	 */
	public boolean printWordState(String word, List<Character> playerGuesses) {
		int correctCount = 0;
		for (int i = 0; i < word.length(); i++) {
			if (playerGuesses.contains(word.charAt(i))) {
				System.out.print(word.charAt(i));
				correctCount++;
			} else {
				System.out.print("_");
			}
		}
		System.out.println();
		return (word.length() == correctCount);
	}

	/**
	 * This function prints the HangMan figure..
	 * @param wrongCount
	 */
	public void printHangedMan(Integer wrongCount, List<String> hangManDraw) {
		System.out.println(" -------");
		System.out.println(" |     |");
		if (wrongCount >= 1) {
			System.out.println(hangManDraw.get(0));
		}
		if (wrongCount >= 2) {
			System.out.print(hangManDraw.get(1));
			if (wrongCount >= 3) {
				System.out.println(hangManDraw.get(2));
			} else {
				System.out.println("");
			}
		}
		if (wrongCount >= 4) {
			System.out.println(hangManDraw.get(3));
		}
		if (wrongCount >= 5) {
			System.out.print(hangManDraw.get(4));
			if (wrongCount >= 6) {
				System.out.println(hangManDraw.get(5));
			} else {
				System.out.println("");
			}
		}
		System.out.println("");
		System.out.println("");
	}

	/**
	 * This function prompts the user to ask if he wants to play Again...
	 * @param keyboard
	 * @return
	 */
	public boolean playAgain(Scanner keyboard) {
		System.out.println("Do you wish to play Again?");
		System.out.println("Please enter y or n");
		String wish = keyboard.next();
		if (!(wish.equals("y") || wish.equals("n"))) {
			System.out.println("Please enter y or n");
			wish = keyboard.next();
		}
		if (wish.equals("n")) {
			return false;
		}
		System.out.println();
		return true;
	}

	/**
	 * This function writes the PlayerDetails into the PlayerDetails file and returns true if a user in the past has a higher score than the current user
	 * @param palyerDetails
	 * @return
	 */
	private boolean writePlayerDetailsIntoFile(String palyerDetails) {
		HangMan myObj = new HangMan();
		//playerDetails is list of earlier user details(name score) added from the playerDetails.txt File.
		List<String> playerDetailsList =myObj.getPlayerList();
		myObj.clearTheFile();
		//paylerDetails is the current user details like [Ishita, 100]
		String[] str = palyerDetails.split("----");
		Integer score = Integer.parseInt(str[1]);

		//Creates a map that stores name and score of the earlier users.
		Map<Object, Object> result = playerDetailsList.stream().map(s -> s.split("----")) 	//returns array of Strings["a","40"]
				.collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : "", (address1, address2) -> address1)); //a[0]: name and a[1]:score

		// If the earlier user score is more than present user score.
		List<Object> earlierUserWon = result.values().stream().filter(value -> Integer.parseInt((String) value) >= score)
				.collect(Collectors.toList());

		File file = new File("resources/playerdetails.txt");
		PrintWriter fw = null;
		try {
			fw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newLine = System.getProperty("line.separator");

		//Adding current user details to the playerDetails List
		playerDetailsList.add(palyerDetails);
		//Writing the PlayerDetails list into the file
		for (String myStr : playerDetailsList) {
			fw.write(myStr + newLine);
		}
		fw.close();
		if (earlierUserWon.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This function clears out the file
	 */
	public void clearTheFile() {
		File file = new File("resources/playerdetails.txt");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * This function gets the hangman Diagram from hangman.txt file in resources folder
	 * @return
	 */
	private List<String> getHangManDraw() {
		try {
			Path curPath = Paths.get("resources/hangman.txt");
			return Files.readAllLines(curPath);
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * This function gets the Player List from playerdetails.txt file in resources folder
	 * @return
	 */
	private List<String> getPlayerList() {
		try {
			return Files.readAllLines(Paths.get("resources/playerdetails.txt"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;

	}
}
