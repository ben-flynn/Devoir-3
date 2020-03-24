
/**
 * The class <b>TicTacToeGame</b> is the
 * class that implements the Tic Tac Toe Game.
 * It contains the grid and tracks its progress.
 * It automatically maintain the current state of
 * the game as players are making moves.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

import java.util.LinkedList;

public class TicTacToeGame {

	/**
	 * The board of the game, stored as a single array.
	 */
	private CellValue[] board;

	/**
	 * level records the number of rounds that have been played so far. Starts at 0.
	 */
	private int level;

	/**
	 * gameState records the current state of the game.
	 */
	private GameState gameState;

	/**
	 * lines is the number of lines in the grid
	 */
	public final int lines;

	/**
	 * columns is the number of columns in the grid
	 */
	public final int columns;

	/**
	 * sizeWin is the number of cell of the same type that must be aligned to win
	 * the game
	 */
	public final int sizeWin;

	/**
	 * transformedBoard is used to iterate through all game symetries. We use an
	 * indirection, so as to not modify the instance variable board. In the current
	 * symmetry the cell at index i is accessed via board[transformedBoard[i]]
	 * transformedBoard is "protected" in anticipation of A4
	 */
	protected int[] transformedBoard;

	// tracks the number of symetrical changes the board has gone through
	// in other words every successful use of the next method

	public int tracker;

	public LinkedList<Transformation> transform;

	/**
	 * default constructor, for a game of 3x3, which must align 3 cells
	 */
	public TicTacToeGame() {
		this(3, 3, 3);
	}

	/**
	 * constructor allowing to specify the number of lines and the number of columns
	 * for the game. 3 cells must be aligned.
	 * 
	 * @param lines   the number of lines in the game
	 * @param columns the number of columns in the game
	 */
	public TicTacToeGame(int lines, int columns) {
		this(lines, columns, 3);
	}

	/**
	 * constructor allowing to specify the number of lines and the number of columns
	 * for the game, as well as the number of cells that must be aligned to win.
	 * 
	 * @param lines   the number of lines in the game
	 * @param columns the number of columns in the game
	 * @param sizeWin the number of cells that must be aligned to win.
	 */
	public TicTacToeGame(int lines, int columns, int sizeWin) {
		this.lines = lines;
		this.columns = columns;
		this.sizeWin = sizeWin;
		board = new CellValue[lines * columns];
		for (int i = 0; i < lines * columns; i++) {
			board[i] = CellValue.EMPTY;
		}
		level = 0;
		gameState = GameState.PLAYING;

		// UPDATE HERE IF NEEDED
	}

	/**
	 * constructor allowing to create a new game based on an exisiting game, on
	 * which one move is added. The resulting new instance is a deep copy of the
	 * game reference passed as parameter.
	 * 
	 * @param base the TicTacToeGame instance on which this new game is based
	 * @param next the index of the next move.
	 */

	public TicTacToeGame(TicTacToeGame base, int next) {

		lines = base.lines;
		columns = base.columns;
		sizeWin = base.sizeWin;

		if (next < 0 || next >= lines * columns) {
			throw new IllegalArgumentException("Illegal position: " + next);
		}

		if (base == null) {
			throw new IllegalArgumentException("Illegal base game: null value");
		}

		if (base.board[next] != CellValue.EMPTY) {
			throw new IllegalArgumentException("CellValue not empty: " + next + " in game " + base);
		}

		board = new CellValue[lines * columns];
		for (int i = 0; i < lines * columns; i++) {
			board[i] = base.board[i];
		}

		level = base.level + 1;

		board[next] = base.nextCellValue();

		if (base.gameState != GameState.PLAYING) {
			System.out.println("hum, extending a finished game... keeping original winner");
			gameState = base.gameState;
		} else {
			setGameState(next);
		}

		// UPDATE HERE IF NEEDED
	}

	/**
	 * Compares this instance of the game with the instance passed as parameter.
	 * Return true if and only if the two instance represent the same state of the
	 * game.
	 * 
	 * @param other the instance to be compared with this one
	 */

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}

		TicTacToeGame other = (TicTacToeGame) o;

		if ((level != other.level) || (lines != other.lines) || (columns != other.columns)
				|| (sizeWin != other.sizeWin)) {
			return false;
		}
		for (int i = 0; i < board.length; i++) {
			if (board[i] != other.board[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * getter for the variable level
	 * 
	 * @return the value of level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * getter for the variable gameState
	 * 
	 * @return the value of gameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * returns the cellValue that is expected next, in other word, which played (X
	 * or O) should play next. This method does not modify the state of the game.
	 * 
	 * @return the value of the enum CellValue corresponding to the next expected
	 *         value.
	 */
	public CellValue nextCellValue() {
		return (level % 2 == 0) ? CellValue.X : CellValue.O;
	}

	/**
	 * returns the value of the cell at index i. If the index is invalid, an error
	 * message is printed out. The behaviour is then unspecified
	 * 
	 * @param i the index of the cell in the array board
	 * @return the value at index i in the variable board.
	 */
	public CellValue valueAt(int i) {

		if (i < 0 || i >= lines * columns) {
			throw new IllegalArgumentException("Illegal position: " + i);
		}

		return board[i];
	}

	/**
	 * This method is call by the next player to play at the cell at index i. If the
	 * index is invalid, an error message is printed out. The behaviour is then
	 * unspecified If the chosen cell is not empty, an error message is printed out.
	 * The behaviour is then unspecified If the move is valide, the board is
	 * updated, as well as the state of the game. To faciliate testing, is is
	 * acceptable to keep playing after a game is already won. If that is the case,
	 * the a message should be printed out and the move recorded. the winner of the
	 * game is the player who won first
	 * 
	 * @param i the index of the cell in the array board that has been selected by
	 *          the next player
	 */
	public void play(int i) {

		if (i < 0 || i >= lines * columns) {
			throw new IllegalArgumentException("Illegal position: " + i);
		}
		if (board[i] != CellValue.EMPTY) {
			throw new IllegalArgumentException("CellValue not empty: " + i + " in game " + toString());
		}

		board[i] = nextCellValue();
		level++;
		if (gameState != GameState.PLAYING) {
			System.out.println("hum, extending a finished game... keeping original winner");
		} else {
			setGameState(i);
		}

	}

	/**
	 * A helper method which updates the gameState variable correctly after the cell
	 * at index i was just set. The method assumes that prior to setting the cell at
	 * index i, the gameState variable was correctly set. it also assumes that it is
	 * only called if the game was not already finished when the cell at index i was
	 * played (the the game was playing). Therefore, it only needs to check if
	 * playing at index i has concluded the game
	 *
	 * @param i the index of the cell in the array board that has just been set
	 */

	private void setGameState(int index) {

		int left = Math.min(sizeWin - 1, index % columns);
		int right = Math.min(sizeWin - 1, columns - (index % columns + 1));
		if ((countConsecutive(index - 1, left, -1, board[index])
				+ countConsecutive(index + 1, right, 1, board[index])) >= sizeWin - 1) {
			setGameState(board[index]);
			return;
		}

		int up = Math.min(sizeWin - 1, index / columns);
		int down = Math.min(sizeWin - 1, lines - (index / columns + 1));
		if ((countConsecutive(index - columns, up, -columns, board[index])
				+ countConsecutive(index + columns, down, columns, board[index])) >= sizeWin - 1) {
			setGameState(board[index]);
			return;
		}

		int upLeft = Math.min(up, left);
		int downRight = Math.min(down, right);
		if ((countConsecutive(index - (columns + 1), upLeft, -(columns + 1), board[index])
				+ countConsecutive(index + (columns + 1), downRight, columns + 1, board[index])) >= sizeWin - 1) {
			setGameState(board[index]);
			return;
		}

		int upRight = Math.min(up, right);
		int downLeft = Math.min(down, left);
		if ((countConsecutive(index - (columns - 1), upRight, -(columns - 1), board[index])
				+ countConsecutive(index + (columns - 1), downLeft, columns - 1, board[index])) >= sizeWin - 1) {
			setGameState(board[index]);
			return;
		}

		if (level == lines * columns) {
			gameState = GameState.DRAW;
		} else {
			gameState = GameState.PLAYING;
		}

	}

	private int countConsecutive(int startingPosition, int numberOfSteps, int stepGap, CellValue value) {

		int result = 0;
		for (int i = 0; i < numberOfSteps; i++) {
			if (board[startingPosition + i * stepGap] != value)
				break;
			result++;
		}
		return result;

	}

	private void setGameState(CellValue value) {
		switch (value) {
			case X:
				gameState = GameState.XWIN;
				break;
			case O:
				gameState = GameState.OWIN;
				break;
			default:
				throw new IllegalArgumentException("cannot set Game State to value " + value);
		}
	}

	/**
	 * Returns a String representation of the game matching the example provided in
	 * the assignment's description
	 *
	 * @return String representation of the game
	 */

	public String toString() {
		String res = "";
		for (int i = 0; i < lines; i++) {
			if (i > 0) {
				for (int j = 0; j < 4 * columns - 1; j++) {
					res += "-";
				}
				res += Utils.NEW_LINE;
			}
			for (int j = 0; j < columns; j++) {
				switch (board[i * columns + j]) {
					case X:
						res += " X ";
						break;
					case O:
						res += " O ";
						break;
					default:
						res += "   ";
				}
				if (j < columns - 1) {
					res += "|";
				} else {
					res += Utils.NEW_LINE;
				}
			}
		}
		return res;

	}

	/**
	 * restarts the list of symmetries
	 */

	public void reset() {
		// initialisation du compteur de rotation ou flip
		tracker = 0;
		// initialisation du tableau d'entiers transformedBoard avec lines*columns comme
		// taille
		transformedBoard = new int[lines * columns];
		// Assignation des nombres de 0 à lines*columns-1
		for (int i = 0; i < transformedBoard.length; i++) {
			transformedBoard[i] = i;
		}

	}

	/**
	 * checks if there are more symmetries to go through
	 *
	 * @return true iff there are additional symmetries
	 */
	public boolean hasNext() {
		/*
		 * Pour des grilles de m par m Retoune false si le tracker est à sa huitième
		 * transformation
		 */
		if (tracker == 8) {
			return false;
		}
		/*
		 * Pour des grilles de m par n Retoune false si le tracker est à sa quatrième
		 * transformation
		 */
		if ((tracker == 4) && (lines != columns)) {
			return false;
		}
		// retourne true dans le cas si aucun des critères si dessus ne sont pas remplis
		return true;
	}

	/**
	 * computes the next symmetries and stores it in the array "transform". Requires
	 * that this.hasNext() == true
	 */
	public void next() throws IllegalStateException {
		// Verifie s'il y a une transformation disponible
		if (this.hasNext()) {
			// Si c'est une grille m par n
			if (lines != columns) {

				switch (tracker) {
					// Pour la première ou troisième transformation
					case 1:
					case 3:
						// Effectue une transformation horizontal
						Utils.horizontalFlip(lines, columns, transformedBoard);

						break;
					// Pour la deuxième transformation
					case 2:
						// Effectue une transformation vertical
						Utils.verticalFlip(lines, columns, transformedBoard);

						break;
					// NB: Lorsque tracker==0, Aucune transformation n'est effectuée
				}
				// Incremente le compteur de transformation
				tracker++;
			}

			// Si c'est une grille m par m
			if (lines == columns) {
				// Pour la 1er, 2e,3e, 5e,6e et 7e transformation
				if ((tracker < 4 && tracker > 0) || tracker > 4) {
					// Effectue une rotation
					Utils.rotate(lines, columns, transformedBoard);
				}
				// Pour la 4e rotation
				if (tracker == 4) {
					// Effectue une transformation horizontal
					Utils.horizontalFlip(lines, columns, transformedBoard);
				}
				// Incremente le compteur de transformation
				tracker++;

			}
		}
		// Si la grille n'a plus de symetries
		else {
			// Lance une IllegalStateException (declaration non obligatoire)
			throw new IllegalStateException();
		}
	}

	/**
	 * Compares this instance of the game with the instance passed as parameter.
	 * Return true if and only if the two instance represent the same state of the
	 * game, up to symmetry.
	 * 
	 * @param other the TicTacToeGame instance to be compared with this one
	 */
	public boolean equalsWithSymmetry(TicTacToeGame other) {
		// Declaration de variables locales
		boolean equals;
		int i;

		// Reset le transformedBoard
		this.reset();
		// Boucle réitérant sur les transformations disponible de la grille
		while (this.hasNext()) {
			// initialise la variable equals à true
			equals = true;
			// Cherche la prochaine transformation
			this.next();
			// initialise l'indice i
			i = 0;
			// Boucle parcourant le tableau de jeu de joueur 1
			for (CellValue v : this.board) {
				// Si la valeur ne correspond pas à celle dans le tableau de jeu du joueur 2
				// avec la transformation
				// equals devient false et sort de la boucle
				if (v != other.board[transformedBoard[i++]]) {
					equals = false;
					break;
				}
			}
			// Si equals est true retourne true
			if (equals) {
				return true;
			}

		}
		// Reourne false si les grilles des 2 joueurs ne sont pas symetriques.
		return false;

	}

	/**
	 * Returns a String representation of the game as currently trasnsformed
	 *
	 * @return String representation of the game
	 */

	public String toStringTransformed() {
		if (transformedBoard == null) {
			throw new NullPointerException("transformedBoard not initialized");
		}

		String res = "";
		for (int i = 0; i < lines; i++) {
			if (i > 0) {
				for (int j = 0; j < 4 * columns - 1; j++) {
					res += "-";
				}
				res += Utils.NEW_LINE;
			}
			for (int j = 0; j < columns; j++) {
				switch (board[transformedBoard[i * columns + j]]) {
					case X:
						res += " X ";
						break;
					case O:
						res += " O ";
						break;
					default:
						res += "   ";
				}
				if (j < columns - 1) {
					res += "|";
				} else {
					res += Utils.NEW_LINE;
				}
			}
		}
		return res;

	}
}
