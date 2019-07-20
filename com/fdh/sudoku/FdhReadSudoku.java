/*
 * Created on 7 janv. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fdh.sudoku;
import com.fhe.cp.*;

import java.io.*;

/**
 * @author Frédéric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FdhReadSudoku {
	FheManager _m;
	FheIntVar [] _values;
	final int nbValues = 81;
	
	FdhReadSudoku(FheManager m)
	{
		long value;
		_m = m;
		_m.addSudoku( this);
		try{
			char c;
			int i,j,k;

			FileReader file = new FileReader("Sudoku.csv");
	        BufferedReader in = new BufferedReader(file);
	        String str,fields[];

			_values   = new FheIntVar[nbValues];
		
			for (i=0;i<nbValues;i++)
				_values[i]   = new FheIntVar(m,1,9,"value "+i);
	        
	        file = new FileReader("Sudoku.csv");
	        in = new BufferedReader(file);

	        i=0;
	        int[]workload;
	        Integer workloaValue;
	          		
	        while ((str = in.readLine()) != null)
	        {	            
	       		fields = str.split(";");
           		            		
            	for(k=0;k<fields.length ;k++)
            	{
            		value = Integer.valueOf( fields[k]).longValue();
            		if (value == 0)
            		{
            			_values[i] = new FheIntVar(m,1,9,"value "+i);
            		}
            		else
            			_values[i] = new FheIntVar(m,value,value,"value "+i);
                	i++;
            	}
	        }  
        	if(i!=nbValues)
        		System.out.println("mauvaise grille");
	
        	// différences par lignes
        	for(i=0;i<9;i++)
        	{
            	FheIntExp line = new FheIntExp(_m,0);
            	for(j=0;j<9;j++)
            		line.add(1,_values[i*9+j]);
        		m.addExpEventDiff(line);
        	}

        	// différences par colonnes
        	for(i=0;i<9;i++)
        	{
            	FheIntExp column = new FheIntExp(_m,0);
            	for(j=0;j<9;j++)
            		column.add(1,_values[i+j*9]);
           		m.addExpEventDiff(column);
        	}

        	// différences par carrés
        	k=0;
        	for(j=0;j<9;j++)
        	{
        		FheIntExp square = new FheIntExp(_m,0);
            	for(i=0;i<3;i++)
            	{
            		square.add(1,_values[k+i]);
            		square.add(1,_values[k+i+9]);
            		square.add(1,_values[k+i+18]);
            	}
        		m.addExpEventDiff(square);
            	k+=3;       	
            	if (k==9)
            		k=27;
            	else if (k==36)
            			k=54;
        	}
        	
        	
        	in.close();
	        file.close();

	    } catch (IOException e) {
			System.err.println("Reading error: " + e);}
    }
	 
	public void printSchedule(FileWriter SolutionToFile)
	{
		try
		{
			int i,j,k;
			int value;
			k=0;
			for(i=0;i<9;i++)
			{
				for(j=0;j<9;j++)
				{
					if (_values[k].size() > 1)
						value = 0;
					else
						value = (int) _values[k].getFirst();
					SolutionToFile.write(value+";");
					k++;
				}
				SolutionToFile.write(13);
			}
		} catch (IOException e) {
			System.err.println("Writing error: " + e);}
		
	}
}
