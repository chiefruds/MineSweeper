
public class Game {
	private MineField myField;
	private int fieldSize;
	
	// a boolean to check if it is turn one
	private boolean turnOne = true;

	// the class constructor that makes the field.
	public Game(int fieldSize, int bombChance) {
		this.fieldSize = fieldSize;
		myField = new MineField(fieldSize, bombChance);
		myField.print(fieldSize);
	}

	// the play method that has all the methods to play the game
	public void play(String input, MineSweeper myMineSweeper) {
		myField.checkField(input, myMineSweeper);
		
		/* in turn one there are no bombs so the method checkWon would make you the winner the moment you start
		 * because of that the method checkWon will only be used if turnOne == false
		 */
		if (!turnOne) {
			myField.checkWon(myMineSweeper);
		}
		
		/*
		 * only if ingame is true and isCanPrint is true will the field be printed
		 * if turnOne is true and the input square is not a mark the bombs will be set and turnOne will be false
		 */
		if (myMineSweeper.isIngame() && myField.isCanPrint()) {
			if (turnOne && !myField.isMarking()) {
					myField.setBombs(input);
					turnOne = false;
			}
			myField.print(fieldSize);
		}
	}
}
