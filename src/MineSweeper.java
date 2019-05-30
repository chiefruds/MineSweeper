import java.util.Scanner;

public class MineSweeper {
	private Game myGame;

	// 4 final integers to set the max size of the field and the max bombChance
	private final int minFieldSize;
	private final int maxFieldSize;
	private final int minBombChance;
	private final int maxBombChance;
	
	//the fieldSize and the bombChance that the player has given to the scanner
	private int fieldSize;
	private int bombChance;

	/*  the boolean ingame checks if the game is still going on
	 *  the boolean playAgain checks if the player wants to playAgain 
	 *  the boolean validatePlayAgain checks if the player has entered yes or no
	 *  the boolean validateInput checks if the player has entered a valid number for the fieldSize or bombChance
	 */
	private boolean ingame;
	private boolean playAgain;
	private boolean validatePlayAgain;
	private boolean validateInput;
	private String input;

	private Scanner sc;

	public MineSweeper() {
		sc = new Scanner(System.in);
		
		minFieldSize = 5;
		maxFieldSize = 20;
		minBombChance = 10;
		maxBombChance = 25;
		
		start();
	}

	public void start() {
		// the loop for starting the game and if the player wants to the loop will start
		// again.
		playAgain = true;
		System.out.println("Welcome to the game minesweeper!");
		System.out.println("Try to find all of the mines in the minefield.");
		while (playAgain) {
			System.out.println("Give the size of the field (5 - 20)");
			validatePlayerInput("fieldSize", minFieldSize, maxFieldSize);
			System.out.println("Give the bomb chance now in % (10 - 25)");
			validatePlayerInput("bombChance", minBombChance, maxBombChance);
			
			// instantiating the game object and giving it the fieldSize and the bombChance
			myGame = new Game(fieldSize, bombChance);

			ingame = true;

			// playing the game until player has won or lost.
			while (ingame) {

				if (ingame == false) {
					break;
				}

				System.out.println("Give the name of the field you want to test or mark (*): ");
				collectInput();
				myGame.play(input, this);
			}

			// validate if the input is yes or no, if not then the loop will repeat.
			validatePlayAgain = true;
			while (validatePlayAgain) {
				System.out.println("Would you like to play another game (yes/no)?");
				collectInput();
				if (input.equals("no")) {
					playAgain = false;
					validatePlayAgain = false;
				} else if (input.equals("yes")) {
					playAgain = true;
					validatePlayAgain = false;
				} else {
					System.out.println("Please answer with yes or no.");
				}
			}
		}
		System.out.println("Thank you for playing the game minesweeper.");
		System.out.println("Hopefully you will play this game soon next time!");

	}

	// validate if the player input is a number.
	private void validatePlayerInput(String boardType, int min, int max) {
		int playerInput = 0;
		validateInput = true;
		boolean isNumber = true;
		while (validateInput) {
			isNumber = true;
			collectInput();
			try {
				playerInput = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				isNumber = false;
			}
			if (playerInput >= min && playerInput <= max && isNumber == true) {
				if (boardType.equals("fieldSize")) {
					fieldSize = playerInput;
					validateInput = false;
				} else if (boardType.equals("bombChance")) {
					bombChance = playerInput;
					validateInput = false;
				} else {
					System.out.println("the board type was wrong");
				}
			} else {
				if(boardType.equals("fieldSize")) {
				System.out.println("*** the size must be between " + min + " and " + max + "! ***");
				} else if (boardType.equals("bombChance")) {
					System.out.print("*** the chance must be between " + min + " and " + max + "! ***");
				}
			}
		}
	}

	// a method to return the boolean ingame
	public boolean isIngame() {
		return ingame;
	}

	// a method to set the ingame boolean
	public void setIngame(boolean ingame) {
		this.ingame = ingame;
	}
	
	// a method to collect the input from the player
	private void collectInput() {
		input = sc.nextLine();
		if(input.isEmpty()) {
			input = sc.nextLine();
		}
		input = input.replaceAll("//s", "");
	}
}
