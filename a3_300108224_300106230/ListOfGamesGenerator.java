import java.util.LinkedList;

public class ListOfGamesGenerator {
	
   /**
	* generates all different games for the specified
	* parameters. Each game is recorded only once. 
	* once a game is finished, it is not extended further
	* @param lines
    *  the number of lines in the game
    * @param columns
    *  the number of columns in the game
    * @param sizeWin
    *  the number of cells that must be aligned to win.
    * @return
    * a list of lists of game instances, ordered by levels
  	*/	
  	public static LinkedList<LinkedList<TicTacToeGame>> generateAllGames(int lines, int columns, int winLength){

		LinkedList<LinkedList<TicTacToeGame>> allGames;

		allGames = new LinkedList<LinkedList<TicTacToeGame>>();

		// start with the empty game
		allGames.add(new LinkedList<TicTacToeGame>());
		allGames.get(0).add(new TicTacToeGame(lines,columns,winLength));

		//build the new games by adding the next moves to the
		// previously built games
		for(int i=1; i<= lines*columns; i++) {
			LinkedList<TicTacToeGame> newList;
			newList = new LinkedList<TicTacToeGame>();
			allGames.add(newList);
			for(TicTacToeGame game: allGames.get(i-1)){
				if(game.getGameState() == GameState.PLAYING) {
					for(int j = 0;
						j < lines*columns;
						j++) {
						if(game.valueAt(j) == CellValue.EMPTY) {
							TicTacToeGame newGame = new TicTacToeGame(game,j);
							//checking that this game was not already found
							boolean isNew = true;
							for(TicTacToeGame existingGame: allGames.get(i)){
								if(newGame.equals(existingGame)){
									isNew = false;
									break;
								}
							}
							if(isNew) {
								newList.add(newGame);
							}					
						}
					}
				}

			}
		}


		return allGames;

	}

   /**
	* generates all different games,
	* up to symmetry. 
    * @return
    * a list of lists of game instances, ordered by levels
  	*/	

  	public static LinkedList<LinkedList<TicTacToeGame>> generateAllUniqueGames(int lines, int columns, int winLength){

		LinkedList<LinkedList<TicTacToeGame>> allGames;

		allGames = new LinkedList<LinkedList<TicTacToeGame>>();

		// start with the empty game
		allGames.add(new LinkedList<TicTacToeGame>());
		allGames.get(0).add(new TicTacToeGame(lines,columns,winLength));

		//build the new games by adding the next moves to the
		// previously built games
		for(int i=1; i<= lines*columns; i++) {
			LinkedList<TicTacToeGame> newList;
			newList = new LinkedList<TicTacToeGame>();
			allGames.add(newList);
			boolean atLeastOneStillPlaying = false;
			for(TicTacToeGame game: allGames.get(i-1)){
				if(game.getGameState() == GameState.PLAYING) {
					for(int j = 0;
						j < lines*columns;
						j++) {
						if(game.valueAt(j) == CellValue.EMPTY) {
							TicTacToeGame newGame = new TicTacToeGame(game,j);
							//checking that a symmetrical game was not already found
							boolean isNew = true;
							for(TicTacToeGame existingGame: allGames.get(i)){
								if(newGame.equalsWithSymmetry(existingGame)){
									isNew = false;
									break;
								}
							}
							if(isNew) {
								newList.add(newGame);
								if(newGame.getGameState() == GameState.PLAYING) {
									atLeastOneStillPlaying = true;
								}
							}
						}
					}
				}
			}
			if(!atLeastOneStillPlaying) {
				break;
			} 
		}

		return allGames;

	}
}