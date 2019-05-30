import java.util.HashMap;
import java.util.Random;

public class MineField {

	/*
	 * myField is the hasmap that holds the field maxColumnChar holds the max char
	 * size of the field the boolean outOfBounds = true if the chosen square is out
	 * of bounds if not then outOfBounds = false the boolean marking = true if the
	 * player has marked or demarked a square
	 */

	private HashMap<String, Square> myField;
	private char maxColumnChar;
	private int fieldSize;
	private int bombChance;
	private boolean outOfBounds;
	private int chosenRowInt;
	private char chosenColumnChar;
	private int totalBombs;
	private boolean canPrint;
	private boolean marking;

	// the constructor that instantiates the HashMap.
	public MineField(int fieldSize, int bombChance) {
		myField = new HashMap<String, Square>();
		makeField(fieldSize, bombChance);
	}

	// this method makes the field.
	public void makeField(int fieldSize, int bombChance) {
		maxColumnChar = 64;
		maxColumnChar += fieldSize;
		this.fieldSize = fieldSize;
		this.bombChance = bombChance;
		String key = "";

		for (int i = fieldSize; i >= 1; i--) {
			for (char c = 'A'; c <= maxColumnChar; c++) {
				if (c >= 'A' && c <= maxColumnChar) {
					key = "" + c + i;
					myField.put(key, new Square());
					myField.get(key).setType("normal");
				}
			}
		}
	}

	/*
	 * a method that will set the bombs in the field it gives a random number
	 * between 100 % and if that is <= the bomb Chance it will place a bomb in that
	 * square
	 */
	public void setBombs(String square) {
		Random rand = new Random();
		for (String key : myField.keySet()) {
			if (!key.equals(square)) {
				if ((rand.nextInt(100) + 1) <= bombChance) {
					myField.get(key).setType("bomb");
					totalBombs++;
				}
				if (totalBombs == 0) {
					int randomColumn = rand.nextInt(fieldSize) + 1;
					char randomChar = (char) (64 + rand.nextInt(fieldSize) + 1);
					String randomKey = "" + randomChar + randomColumn;
					myField.get(randomKey).setType("bomb");
					totalBombs++;
				}
				if (myField.get(square).getType().equals("bomb")) {
					myField.get(square).setType("tested");
				}
			}
		}
		checkBombCount(square);
	}

	// a method to print the game.
	public void print(int fieldSize) {
		char row = 64;
		row += fieldSize;
		String key = "";

		for (int i = fieldSize; i >= 1; i--) {
			System.out.println();

			if (i > 9) {
				System.out.print(i + " ");
			} else {
				System.out.print(" " + i + " ");
			}

			for (char c = 'A'; c <= row; c++) {
				if (c >= 'A' && c <= row) {
					key = "" + c + i;
					if (myField.get(key).isMarked()) {
						System.out.print("*");
					} else if (myField.get(key).getType().equals("bomb")) {
						System.out.print(".");
					} else if (myField.get(key).getType().equals("normal")) {
						System.out.print(".");
					} else if (myField.get(key).getType().equals("tested")) {
						System.out.print("" + myField.get(key).getBombCount());
					}
				}
			}
		}
		System.out.println();
		System.out.print("   ");
		for (char c = 'A'; c <= row; c++) {
			System.out.print(c);
		}
		System.out.println();

		if (Main.cheatMode) {
			printCheatMode(fieldSize);
		}
	}

	// a method to print the cheat field.
	public void printCheatMode(int fieldSize) {
		char row = 64;
		row += fieldSize;
		String key = "";

		for (int i = fieldSize; i >= 1; i--) {
			System.out.println();

			if (i > 9) {
				System.out.print(i + " ");
			} else {
				System.out.print(" " + i + " ");
			}

			for (char c = 'A'; c <= row; c++) {
				if (c >= 'A' && c <= row) {
					key = "" + c + i;
					if (myField.get(key).getType().equals("bomb")) {
						System.out.print("*");
					} else {
						System.out.print("~");
					}
				}
			}
		}
		System.out.println();
		System.out.print("   ");
		for (char c = 'A'; c <= row; c++) {
			System.out.print(c);
		}
		System.out.println();
	}

	/*
	 * the method that decides what happens when a player clicks on a square. marked
	 * contains the first character of the user input markedSquare contains the key
	 * for the square if the user wants to mark it
	 */
	public void checkField(String square, MineSweeper myMineSweeper) {
		String marked = "";
		String markedSquare;
		marking = false;
		canPrint = true;
		try {
			marked = square.substring(0, 1);
		} catch (StringIndexOutOfBoundsException e) {

		}

		checkOutOfBounds(square);

		// if the square is out of bounds it will give the user a error message and the
		// field won't print
		if (outOfBounds) {
			System.out.println("*** the field you have entered doesn't exist ***");
			canPrint = false;
		} else if (marked.equals("*")) {

			// if a player enters a * with a square that square will be marked or demarked or it will print a message.
			markedSquare = square.substring(1, square.length());
			if (!myField.get(markedSquare).getType().equals("tested")) {
				if (myField.get(markedSquare).isMarked()) {
					myField.get(markedSquare).setMarked(false);
					marking = true;
				} else {
					myField.get(markedSquare).setMarked(true);
					marking = true;
				}
			} else {
				System.out.println("*** You have already tested that location ***");
				canPrint = false;
			}
		} else if (myField.get(square).getType().equals("bomb")) {
			// if the square that the player has chosen is a bomb then he will get a lose message and the game will end
			System.out.println("BOOM! Unfortunatly you lost the game.");
			canPrint = false;
			myMineSweeper.setIngame(false);
		} else if (myField.get(square).getType().equals("normal")) {
			// if the square that the player has chosen is a normal square than the type will be changed to tested
			checkBombCount(square);
			myField.get(square).setType("tested");
			if (myField.get(square).isMarked()) {
				myField.get(square).setMarked(false);
			}
		} else {
			System.out.println("*** You have already tested that location ***");
			canPrint = false;
		}
	}

	// a method to separate the user input into a character and a integer.
	private void separateSquareKey(String square) {
		String outOfBoundsRow = "";
		try {
			outOfBoundsRow = square.substring(1, square.length());
			chosenColumnChar = square.charAt(0);
		} catch (StringIndexOutOfBoundsException e) {

		}
		chosenRowInt = 0;

		if (chosenColumnChar == '*') {
			try {
				outOfBoundsRow = square.substring(2, square.length());
				chosenColumnChar = square.charAt(1);
			} catch (StringIndexOutOfBoundsException e) {

			}
		}

		try {
			chosenRowInt = Integer.parseInt(outOfBoundsRow);
		} catch (NumberFormatException e) {
			outOfBounds = true;
		}
	}

	// a method to check how many bombs each square has lying around it.
	private void checkBombCount(String square) {
		int bombCount = 0;
		String bombCheckSquare = "";

		if ((chosenRowInt - 1) <= fieldSize && (chosenRowInt - 1) >= 1) {
			bombCheckSquare = "" + chosenColumnChar + (chosenRowInt - 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if ((chosenRowInt + 1) <= fieldSize && (chosenRowInt + 1) >= 1) {
			bombCheckSquare = "" + chosenColumnChar + (chosenRowInt + 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if ((chosenColumnChar - 1) <= maxColumnChar && (chosenColumnChar - 1) >= 'A') {
			bombCheckSquare = "" + ((char) (chosenColumnChar - 1)) + (chosenRowInt);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if ((chosenColumnChar + 1) <= maxColumnChar && (chosenColumnChar + 1) >= 'A') {
			bombCheckSquare = "" + ((char) (chosenColumnChar + 1)) + (chosenRowInt);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if (((chosenColumnChar + 1) <= maxColumnChar && (chosenColumnChar + 1) >= 'A')
				&& (chosenRowInt + 1) <= fieldSize && (chosenRowInt + 1) >= 1) {

			bombCheckSquare = "" + ((char) (chosenColumnChar + 1)) + (chosenRowInt + 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if (((chosenColumnChar + 1) <= maxColumnChar && (chosenColumnChar + 1) >= 'A')
				&& (chosenRowInt - 1) <= fieldSize && (chosenRowInt - 1) >= 1) {

			bombCheckSquare = "" + ((char) (chosenColumnChar + 1)) + (chosenRowInt - 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if (((chosenColumnChar - 1) <= maxColumnChar && (chosenColumnChar - 1) >= 'A')
				&& (chosenRowInt + 1) <= fieldSize && (chosenRowInt + 1) >= 1) {

			bombCheckSquare = "" + ((char) (chosenColumnChar - 1)) + (chosenRowInt + 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}
		if (((chosenColumnChar - 1) <= maxColumnChar && (chosenColumnChar - 1) >= 'A')
				&& (chosenRowInt - 1) <= fieldSize && (chosenRowInt - 1) >= 1) {

			bombCheckSquare = "" + ((char) (chosenColumnChar - 1)) + (chosenRowInt - 1);
			if (myField.get(bombCheckSquare).getType().equals("bomb")) {
				bombCount++;
			}
		}

		if (!bombCheckSquare.equals("")) {
			myField.get(square).setBombCount(bombCount);
		}
	}

	// a method to check if the user input is correct
	private void checkOutOfBounds(String square) {

		separateSquareKey(square);

		if (Character.isLetter(chosenColumnChar)) {
			if (chosenRowInt > fieldSize || chosenColumnChar > maxColumnChar || chosenRowInt <= 0) {
				outOfBounds = true;
			} else {
				outOfBounds = false;
			}
		} else {
			outOfBounds = true;
		}
	}

	// a method to check if the player has won
	public void checkWon(MineSweeper myMineSweeper) {
		int markedBombsCount = 0;
		int markedSquaresCount = 0;
		for (String key : myField.keySet()) {
			if (myField.get(key).isMarked() && myField.get(key).getType().equals("bomb")) {
				markedBombsCount++;
			}
			if (myField.get(key).isMarked()) {
				markedSquaresCount++;
			}
		}
		if (markedBombsCount == totalBombs && markedBombsCount == markedSquaresCount) {
			System.out.println("*** Congratulations, you have found all of the boms! ***");
			myMineSweeper.setIngame(false);
		}
	}

	public boolean isCanPrint() {
		return canPrint;
	}

	public boolean isMarking() {
		return marking;
	}

}
