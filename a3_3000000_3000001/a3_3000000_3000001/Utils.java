import java.util.Random;
import java.io.Console;

public class Utils {
	public static final Random generator = new Random();
	public static final Console console = System.console();
	public static final String NEW_LINE = System.getProperty("line.separator");


    /**
     * This method rotates array of size lines * columns referenced
     * by transformedBoard by 90 degree clockwise. For example, the
     * 3*3 game
     *
     * 1 | 2 | 3
     * ----------
     * 4 | 5 | 6
     * ----------
     * 7 | 8 | 9
     *
     * for which lines = 3 and columns = 3 is represented by the array
     * transformedBoard = {1,2,3,4,5,6,7,8,9}
     *
     * the method rotates it into the game
     *
     * 7 | 4 | 1
     * ----------
     * 8 | 5 | 2
     * ----------
     * 9 | 6 | 3
     *
     * that is, after the method, transformedBoard = {7,4,1,8,5,2,9,6,3}
     *
     * @param lines
     *  the number of lines in the game
     * @param columns
     *  the number of columns in the game
     * @param transformedBoard
     *  Reference to a 1 dimentional array of size columns x lines
     */


    public static void rotate(int lines, int columns, int[] transformedBoard){

    int [] tempBoard = new int[(lines*columns)];
	  int counter = 0;

    for(int i = 0; i < columns; i++){
     for(int j = 0; j < lines; j++){
			 tempBoard[j + counter] = transformedBoard[((columns * (lines - 1)) + i) - (j * (columns))];

		}
		counter = counter + lines;
		}

    for (int i = 0; i < tempBoard.length; i++){
		  transformedBoard[i] = tempBoard[i];
		}
    }

    /**
     * This method does an horizontal symmetry on array of size lines * columns referenced
     * by transformedBoard. For example, the
     * 3*3 game
     *
     *
     * 1 | 2 | 3
     * ----------
     * 4 | 5 | 6
     * ----------
     * 7 | 8 | 9
     *
     * for which lines = 3 and columns = 3 is represented by the array
     * transformedBoard = {1,2,3,4,5,6,7,8,9}
     *
     * the method transforms it into the game
     *
     * 7 | 8 | 9
     * ----------
     * 4 | 5 | 6
     * ----------
     * 1 | 2 | 3
     *
     * that is, after the method, transformedBoard = {7,8,9,4,5,6,1,2,3}
     *
     * @param lines
     *  the number of lines in the game
     * @param columns
     *  the number of columns in the game
     * @param transformedBoard
     *  Reference to a 1 dimentional array of size columns x lines
     */

    public static  void horizontalFlip(int lines, int columns, int[] transformedBoard){

			int [] tempBoard = new int[(lines*columns)];
		  int counter = 0;

			for(int i = 0; i < lines; i++){
			 for(int j = 0; j < columns; j++){

      tempBoard[j + counter] = transformedBoard[((columns * (lines - 1)) + j) - (i * (columns))];
    }
    counter = counter + columns;
	}


	for (int i = 0; i < tempBoard.length; i++){
		transformedBoard[i] = tempBoard[i];
	}
	}
   /**
     * This method does an vertical symmetry on array of size lines * columns referenced
     * by transformedBoard. For example, the
     * 3*3 game
     *
     * 1 | 2 | 3
     * ----------
     * 4 | 5 | 6
     * ----------
     * 7 | 8 | 9
     *
     * for which lines = 3 and columns = 3 is represented by the array
     * transformedBoard = {1,2,3,4,5,6,7,8,9}
     *
     * the method transforms it into the game
     *
     * 3 | 2 | 1
     * ----------
     * 6 | 5 | 4
     * ----------
     * 9 | 8 | 7
     *
     * that is, after the method, transformedBoard = {3,2,1,6,5,4,9,8,7}
     *
     * @param lines
     *  the number of lines in the game
     * @param columns
     *  the number of columns in the game
     * @param transformedBoard
     *  Reference to a 1 dimentional array of size columns x lines
     */

    public static  void verticalFlip(int lines, int columns, int[] transformedBoard){

			int [] tempBoard = new int[(lines*columns)];
			int counter = 0;

			for(int i = 0; i < lines; i++){
			 for(int j = columns - 1; j >= 0; j--){
       tempBoard[counter++] = transformedBoard[j + (i*columns)];
    }
}
for (int i = 0; i < tempBoard.length; i++){
	transformedBoard[i] = tempBoard[i];
}
}
    private static void test(int lines, int columns){
    	int[] test;
    	test = new int[lines*columns];
    	for(int i = 0 ; i < test.length; i++){
    		test[i] = i;
    	}
    	System.out.println("testing " + lines + " lines and " + columns + " columns.");
    	System.out.println(java.util.Arrays.toString(test));
    	horizontalFlip(lines,columns,test);
    	System.out.println("HF => " + java.util.Arrays.toString(test));
    	horizontalFlip(lines,columns,test);
    	System.out.println("HF => " + java.util.Arrays.toString(test));
    	verticalFlip(lines,columns,test);
    	System.out.println("VF => " + java.util.Arrays.toString(test));
    	verticalFlip(lines,columns,test);
    	System.out.println("VF => " + java.util.Arrays.toString(test));
    	if(lines == columns){
    		for(int i = 0; i < 4; i++) {
		    	rotate(lines,columns,test);
		    	System.out.println("ROT => " + java.util.Arrays.toString(test));
    		}
    	}
    }

    public static void main(String[] args){
    	int[] test;
    	int lines, columns;

    	test(2,2);
    	test(2,3);
    	test(3,3);
    	test(4,3);
    	test(4,4);


    }
}
