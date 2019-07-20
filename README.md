# comparable
Constrain Programming engine with examples

The SUDOKU example:

-Enter a SUDOKU puzzle in a 9X9 grid with 0 in unknown entries
-Save as comparable/sudoku.csv

		Cf. comparable/sudoku.csv example.
		
-Run FdhTheSudoku.java
-See the solution in comparable/Solution_sudoku.csv

This is resolved by:
1- Determine the remaining possible values in each unknown entry 		--constraint propagation
2- Choose a possible value in an unknown entry 							--choice point
3- Updating the remaining possible values in each unknown entry 		--constraint propagation
4- If no more unknown entry: solved
5- ElseIf all entries still have at least one possible value: goto 2 	--recursive graph search
6- Else one entry does not have any possible value left: 				--backtrack to last choice point
	backtrack to the latest choice done in step 2;
	remove that choice from the possible values; 
	execute 2 again
 
This algorithm is based on a generic constraint programming (com.fhe.cp) engine that:
- propagates constraints to reduce the possible values of the variables to a minimum
- implements a recursive search on the graph of the variable possible values with choice points
- implements a backtrack mechanism on fails that restores the state of the graph at the last choice point
