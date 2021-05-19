/*  @Author Israel Wilson Agostinho

------- Exercise ---------------------------------------------
# Looking for Bananas in the Monkeys island #
Given a Jungle in the middle of an island of n*m dimensions. Each field in this jungle contains a
positive integer which is the amount of bananas that exist in that field. Initially the monkey is at first
column but can be at any row. The monkey can move only (right->,right up /,right down\) from a given
cell. Find out the maximum amount of bananas they can collect.

>> Input/Output Examples:

Input : mat[][] = {{1, 3, 3},
{2, 1, 4},
{0, 6, 4}};
Output : 12
{(1,0)->(2,1)->(2,2)}

Input: mat[][] = { {1, 3, 1, 5},
{2, 2, 4, 1},
{5, 0, 2, 3},
{0, 6, 1, 2}};
Output: 16
(2,0) -> (1,1) -> (1,2) -> (0,3) OR
(2,0) -> (3,1) -> (2,2) -> (2,3)

Input: mat[][] = {{10, 33, 13, 15},
{22, 21, 04, 1},
{5, 0, 2, 3},
{0, 6, 14, 2}};
Output: 83
--------------------------------------------------------
    The exercise proposed to find the maximum number of bananas a monkey can
    collect, starting from column 0 of a matrix, following predefined directions 
    (right, right up, right down).
    We can consider each value in the matrix as the number of bananas in that position.
    Code works for a matrix nxm
    > Steps:
        1. We find the biggest value among the three adjacent positions allowed. The recursive method findBestPath will do this logic.
        2. At every visited position, the algorithm will save the total amount of bananas from that position to the end column.
        2. We need a loop to go through all rows so that we can find the best path to follow
        3. Performance is increased by the 'visit' matrix. It avoids the code of running again through a path that was already calculated.
             If it's detected that that position was already visited, we return the biggest sum from that position on, that is already calculated.
 */
 
public class Main
{
    //***** Test mass 1 - uncomment the one you want to test
    
    // static Integer bananas[][] = {{1,2,3,9,2,7,8,10,0,2,7,8,10,5,1,9,1,7,8,10},
    //                               {4,7,9,18,1,7,8,10,0,2,7,8,10,5,1,9,1,7,8,10},
    //                               {7,8,10,0,2,7,8,10,0,2,7,8,10,0,2,7,8,10,0,2},
    //                               {7,8,10,7,8,10,0,2,7,8,10,0,25,1,9,1,7,8,10},
    //                               {7,8,10,9,1,7,8,7,8,10,0,2,7,8,10,0,2,10,0,2},
    //                               {4,7,9,18,1,7,8,10,0,2,7,8,10,0,29,1,7,8,10},
    //                               {7,8,10,0,2,9,1,7,8,10,0,2,7,8,10,0,2,7,8,10},
    //                               {7,8,10,5,1,7,7,8,10,0,2,7,8,10,0,28,10,0,2},
    //                               {7,8,10,9,1,9,7,8,10,0,2,1,7,7,8,10,0,2,8,10},
    //                               {4,7,9,7,8,10,0,2,18,1,7,8,10,0,2,7,8,10,0,2},
    //                               {7,7,8,10,0,2,7,8,10,0,28,10,0,2,9,1,7,8,10},
    //                               {7,8,10,5,7,8,10,0,2,1,7,8,7,8,10,0,2,10,0,2},
    //                               {7,8,10,9,1,9,1,7,7,8,10,0,2,7,8,10,0,2,8,10},
    //                               {7,8,10,5,1,7,7,8,10,0,2,7,8,10,0,28,10,0,2},
    //                               {7,8,10,9,1,11,7,8,10,0,2,1,7,7,8,10,0,2,8,10},
    //                               {4,7,9,7,8,11,0,2,18,1,7,8,10,0,2,7,8,10,0,2},
    //                               {7,7,8,10,0,11,7,8,10,0,28,10,0,2,9,1,7,8,10},
    //                               {7,8,10,5,7,11,10,0,2,1,7,8,7,8,10,0,2,10,0,2},
    //                               {7,8,10,9,1,11,1,7,7,8,10,0,2,7,8,10,0,2,8,10},
    //                               {7,8,10,9,1,11,1,7,7,8,10,0,2,7,8,10,0,2,8,10}
    // };
    
    ////Test Mass2
    // static Integer bananas[][] = {{1,2,3,9,2},
    //                               {4,7,9,18,1},
    //                               {7,8,10,0,2},
    //                               {7,8,10,5,1},
    //                               {7,8,10,9,1},
    //                               {4,7,9,18,1},
    //                               {7,8,10,0,2},
    //                               {7,8,10,5,1},
    //                               {7,8,10,9,1},
    //                               {4,7,9,18,1},
    //                               {7,8,10,0,2},
    //                               {7,8,10,5,1},
    //                               {7,8,10,9,1}};

    // //Test mass 3
    // static Integer bananas[][] =   {{10, 33, 13, 15},
    //                                 {22, 21, 04, 1},
    //                                 {5, 0, 2, 3},
    //                                 {0, 6, 14, 2}};
    
    //I built this method to fill matrix of nxm dimensions. You can change according to your needs.
    static Integer bananas[][] = fillMatrix(4,80);
    
    //-------------------------------------------------------
    //This matrix has the same dimension of the bananas matrix
    //each position from bestSum[][] will carry the max sum from that position until the end column
    public static Integer visited[][] = new Integer[bananas.length][bananas[0].length];
    public static int process = 0;
	public static void main(String[] args) {
	    int maxBananas = 0;

	    //max length is the number of rows
		for(int i = 0; i < bananas.length; i++){
		    int sum;
		    int[] pos = {i,0};
		    
		    //sum first value with the return value
		    sum = bananas[i][0] + findBestPath(pos);
		    
		    //if it's running the first iteration, define maxBananas as the current sum
		    if(i == 0){
		        maxBananas = sum;
		    }else if(sum > maxBananas){
		        maxBananas = sum;
		    }
		    //System.out.println("start at row = " + i + " | sum: " + sum);
		}
		System.out.println(">>> Max number of Bananas: " + maxBananas + " | N elements: " + bananas.length*bananas[0].length + " | Process: " + process);
	}
	/* This recursive method is designed to find the biggest sum of values from a start position
	   startPos : is list with 2 values only.
	              Position 0 is the row, Position 1 is the Column of the matrix
	*/
	public static int findBestPath(int[] startPos)
	{
	    
	    //# PERFORMANCE IMPROVEMENT
	    //Validates if the current position was already visided
	    //If it was visited, we don't need to run all the whole method again
	    //because we already have the biggest sum from this position on.
	    if(visited[startPos[0]][startPos[1]] != null)
	    {
	        return visited[startPos[0]][startPos[1]];
	    }
	    
	    //Here we define the adjacent positions we have to evaluate,
	    //considering the exercise rules.
	    int[] right = {startPos[0], startPos[1] + 1};
	    int[] rightUp = {startPos[0] + 1, startPos[1] + 1};
	    int[] rightDown = {startPos[0] - 1, startPos[1] + 1};
	    
	    //this variable will carry the max value among the adjacent evaluated values.
	    int maxValue = 0;
	    
	    //This variable will carry the next position that will be evaluated on the next run
	    int[] nextPosToEvaluate = {-1,-1};
	    
	    //We need a try catch around each validation, so that we can catch ArrayIndexOutOfBoundsException
	    //This exception will happen when the algorith tries to evaluates a position that is out of the matrix
	    try{
	        maxValue = bananas[right[0]][right[1]];
            nextPosToEvaluate = right;
	    }catch(ArrayIndexOutOfBoundsException e){}
	    try{
    	    if(bananas[rightUp[0]][rightUp[1]] > maxValue){
	            maxValue = bananas[rightUp[0]][rightUp[1]];
	            nextPosToEvaluate = rightUp;
	        }
	    }catch(ArrayIndexOutOfBoundsException e){}
	    try{
    	    if(bananas[rightDown[0]][rightDown[1]] > maxValue){
	            maxValue = bananas[rightDown[0]][rightDown[1]];
	            nextPosToEvaluate = rightDown;
	        }
	    }catch(ArrayIndexOutOfBoundsException e){}
	    
	    //if nextPosToEvaluate[0] is NOT -1, it means that there is a position
	    //to be evaluated, so we call the method again
	    
	    //if nextPosToEvaluate[0] is -1, it means that no position is available
        
	    if(nextPosToEvaluate[0] != -1){
	        visited[startPos[0]][startPos[1]] = maxValue + findBestPath(nextPosToEvaluate);
	    }else{
	        visited[startPos[0]][startPos[1]] = maxValue;
	    }
	    process++;
	    return visited[startPos[0]][startPos[1]];
	}
	
	public static Integer[][] fillMatrix(int l, int c)
	{
	    Integer [][] matrix = new Integer[l][c];
        for(int i = 0; i < l; i++) {
            for(int j = 0; j < c; j++) {
                // read information from somewhere
                matrix[i][j] = 10000*(i+j)/(1+i+j);
            }
        }
        return matrix;
	}
}
