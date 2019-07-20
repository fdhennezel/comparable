/*
 * Created on 7 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.sudoku;
import java.io.FileWriter;
import java.io.IOException;

import com.fhe.cp.*;


/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhTheSudoku{
	   public static void main(String args[]) {
	
	   	try
		{
			int i;
		    FileWriter SolutionToFile = new FileWriter("Solution_sudoku.csv");
		   	FheManager m = new FheManager();
		   	FdhReadSudoku sudoku = new FdhReadSudoku(m);
		   	
		   	boolean fail = m.SearchMinSizeMinVal();
		   	
		   	if (fail == false) 
		   		System.out.println("Solution trouvée en "+m.getFailNb()+" avec "+m.getNbChoicePoint()+" points de choix");
			

		   	sudoku.printSchedule(SolutionToFile);
	   
		   	SolutionToFile.close();
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
	

	   }
}
