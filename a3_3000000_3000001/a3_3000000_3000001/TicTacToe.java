import java.util.LinkedList;

public class TicTacToe{
	
	public static void main(String[] args){

        StudentInfo.display();

        int lines = 3;
        int columns = 3;
        int win = 3;

   
        try{
            if (args.length >= 2) {
                lines = Integer.parseInt(args[0]);
                if(lines<2){
                    System.out.println("Invalid argument, using default...");
                    lines = 3;
                }
                columns = Integer.parseInt(args[1]);
                if(columns<2){
                    System.out.println("Invalid argument, using default...");
                    columns = 3;
                }
            }
            if (args.length >= 3){
                win = Integer.parseInt(args[2]);
                if(win<2){
                    System.out.println("Invalid argument, using default...");
                    win = 3;
                }
            } 
            if (args.length > 3){
                System.out.println("Too many arguments. Only the first 3 are used.");
            } 

        } catch(NumberFormatException e){
            System.out.println("Invalid argument, using default...");
            lines   = 3;
            columns  = 3;
            win = 3;
        }
 


        Long timeStarted = System.currentTimeMillis();

		LinkedList<LinkedList<TicTacToeGame>> allGames;
		allGames = ListOfGamesGenerator.generateAllUniqueGames(lines, columns, win);
		int total = 0;
		int level = 0;
		int wonByX = 0;
		int wonByO = 0;
		int isADraw = 0;
		for(LinkedList<TicTacToeGame> list: allGames){
			System.out.print("======= level " + level + " =======: " + list.size() + " element(s)");
			total += list.size();
			level++;
			int unfinishedGames = 0;
			for(TicTacToeGame game: list){
				if(game.getGameState() == GameState.XWIN) {
					wonByX++;
				} else if (game.getGameState() == GameState.OWIN) {
					wonByO++;
				} else if (game.getGameState() == GameState.DRAW) {
					isADraw++;
				} else {
					unfinishedGames++;
				}
				//System.out.print("\n"+game);
			}
			System.out.println (" (" + unfinishedGames + " still playing)");
		}
		System.out.println("that's " + total + " games");
		System.out.println(wonByX + "  won by X");
		System.out.println(wonByO + "  won by O");
		System.out.println(isADraw + " draw");
		System.out.println("It took " + (System.currentTimeMillis()-timeStarted) + "ms.");


	}
}